/*
 * friendlist.c - [Starting code for] a web-based friend-graph manager.
 *
 * Based on:
 *  tiny.c - A simple, iterative HTTP/1.0 Web server that uses the 
 *      GET method to serve static and dynamic content.
 *   Tiny Web server
 *   Dave O'Hallaron
 *   Carnegie Mellon University
 */
#include "csapp.h"
#include "dictionary.h"
#include "more_string.h"

static void doit(int fd);
static dictionary_t *read_requesthdrs(rio_t *rp);
static void read_postquery(rio_t *rp, dictionary_t *headers, dictionary_t *d);
static void clienterror(int fd, char *cause, char *errnum, 
                        char *shortmsg, char *longmsg);
static void print_stringdictionary(dictionary_t *d);
static void serve_requestFriends(int fd, dictionary_t *query);
static void serve_requestBefriend(int fd, dictionary_t *query);
static void serve_requestUnfriend(int fd, dictionary_t *query);
static void serve_requestIntroduce(int fd, dictionary_t *query);
// Helper for creating threads, manages concurrency
static void *threadCreation(void *connectionFDP);

// Dictionary of the user's friends
dictionary_t* friendsOfUser;
// The program's main thread, needed so we can lock when modifying dictionaries/other data
pthread_mutex_t mainThread;

int main(int argc, char **argv) {
  int listenfd, connfd;
  char hostname[MAXLINE], port[MAXLINE];
  socklen_t clientlen;
  struct sockaddr_storage clientaddr;

  /* Check command line args */
  if (argc != 2) {
    fprintf(stderr, "usage: %s <port>\n", argv[0]);
    exit(1);
  }

  pthread_mutex_init(&mainThread, NULL);
  listenfd = Open_listenfd(argv[1]);
  friendsOfUser = (dictionary_t*)make_dictionary(COMPARE_CASE_SENS, free);

  /* Don't kill the server if there's an error, because
     we want to survive errors due to a client. But we
     do want to report errors. */
  exit_on_error(0);

  /* Also, don't stop on broken connections: */
  Signal(SIGPIPE, SIG_IGN);

  while (1) {
    clientlen = sizeof(clientaddr);
    connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);
    if (connfd >= 0) {
      Getnameinfo((SA *) &clientaddr, clientlen, hostname, MAXLINE, 
                  port, MAXLINE, 0);
      printf("Accepted connection from (%s, %s)\n", hostname, port);

      // Create a separate thread to work on the given job
      int *connectionFDP;
      pthread_t thread;
      connectionFDP = malloc(sizeof(int));
      *connectionFDP = connfd;
      Pthread_create(&thread, NULL, threadCreation, connectionFDP);
      // Detach to automatically reap/free memory when done
      Pthread_detach(thread);
    }
  }
}

/*
 * doit - handle one HTTP request/response transaction
 */
void doit(int fd) {
  char buf[MAXLINE], *method, *uri, *version;
  rio_t rio;
  dictionary_t *headers, *query;

  /* Read request line and headers */
  Rio_readinitb(&rio, fd);
  if (Rio_readlineb(&rio, buf, MAXLINE) <= 0)
    return;
  printf("%s", buf);
  
  if (!parse_request_line(buf, &method, &uri, &version)) {
    clienterror(fd, method, "400", "Bad Request",
                "Friendlist did not recognize the request");
  } else {
    if (strcasecmp(version, "HTTP/1.0")
        && strcasecmp(version, "HTTP/1.1")) {
      clienterror(fd, version, "501", "Not Implemented",
                  "Friendlist does not implement that version");
    } else if (strcasecmp(method, "GET")
               && strcasecmp(method, "POST")) {
      clienterror(fd, method, "501", "Not Implemented",
                  "Friendlist does not implement that method");
    } else {
      headers = read_requesthdrs(&rio);

      /* Parse all query arguments into a dictionary */
      query = make_dictionary(COMPARE_CASE_SENS, free);
      parse_uriquery(uri, query);
      if (!strcasecmp(method, "POST"))
        read_postquery(&rio, headers, query);

      /* For debugging, print the dictionary */
      print_stringdictionary(query);

      /* Handle the appropriate command requested. Also lock the main thread to maintain proper
       data control and avoid possible race conditions!*/
      if(starts_with("/friends", uri))
	{
	  pthread_mutex_lock(&mainThread);
	  serve_requestFriends(fd, query);
	  pthread_mutex_unlock(&mainThread);
	}

      else if(starts_with("/befriend", uri))
	{
	  pthread_mutex_lock(&mainThread);
	  serve_requestBefriend(fd, query);
	  pthread_mutex_unlock(&mainThread);
	}

      else if(starts_with("/unfriend", uri))
	{
	  pthread_mutex_lock(&mainThread);
	  serve_requestUnfriend(fd, query);
	  pthread_mutex_unlock(&mainThread);
	}

      else if(starts_with("/introduce", uri))
	{
	  serve_requestIntroduce(fd, query);
	}

      /* Clean up */
      free_dictionary(query);
      free_dictionary(headers);
    }

    /* Clean up status line */
    free(method);
    free(uri);
    free(version);
  }
}

/*
 * read_requesthdrs - read HTTP request headers
 */
dictionary_t *read_requesthdrs(rio_t *rp) {
  char buf[MAXLINE];
  dictionary_t *d = make_dictionary(COMPARE_CASE_INSENS, free);

  Rio_readlineb(rp, buf, MAXLINE);
  printf("%s", buf);
  while(strcmp(buf, "\r\n")) {
    Rio_readlineb(rp, buf, MAXLINE);
    printf("%s", buf);
    parse_header_line(buf, d);
  }
  
  return d;
}

void read_postquery(rio_t *rp, dictionary_t *headers, dictionary_t *dest) {
  char *len_str, *type, *buffer;
  int len;
  
  len_str = dictionary_get(headers, "Content-Length");
  len = (len_str ? atoi(len_str) : 0);

  type = dictionary_get(headers, "Content-Type");
  
  buffer = malloc(len+1);
  Rio_readnb(rp, buffer, len);
  buffer[len] = 0;

  if (!strcasecmp(type, "application/x-www-form-urlencoded")) {
    parse_query(buffer, dest);
  }

  free(buffer);
}

static char *ok_header(size_t len, const char *content_type) {
  char *len_str, *header;
  
  header = append_strings("HTTP/1.0 200 OK\r\n",
                          "Server: Friendlist Web Server\r\n",
                          "Connection: close\r\n",
                          "Content-length: ", len_str = to_string(len), "\r\n",
                          "Content-type: ", content_type, "\r\n\r\n",
                          NULL);
  free(len_str);

  return header;
}

/*
 * Handles the friends request, which simply lists the friends of a given user.
 */
static void serve_requestFriends(int fd, dictionary_t *query) {
  size_t len;
  char *body, *header;

  if(dictionary_count(query) != 1)
    {
      clienterror(fd, "GET", "400", "Bad Request", "Command requires a user");
    }

  const char *user = dictionary_get(query, "user");
  
  body = "";

  if(user == NULL)
    {
      clienterror(fd, "GET", "400", "Bad Request", "User is invalid/does not exist!");
    }

  dictionary_t *userDict = dictionary_get(friendsOfUser, user);

  // Get the names from the keys of the dictionary, append them with newlines as needed
  if(userDict != NULL)
    {
      const char **listOfUserFriends = dictionary_keys(userDict);
      body = join_strings(listOfUserFriends, '\n');
    }

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/html; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);

}

/*
 * Handles the befriend request, which allows a user to become friends with one or more other users.
 */
static void serve_requestBefriend(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  if(query == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Empty query!");
      return;
    }

  if(dictionary_count(query) != 2)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Not enough arguments for the query!");
    }

  const char* user = (char *)dictionary_get(query, "user");
  
  if(user == NULL)
    {
      dictionary_t* newUser = (dictionary_t *)make_dictionary(0, free);
      dictionary_set(friendsOfUser, user, newUser);
    }

  dictionary_t* actualUser = (dictionary_t *)dictionary_get(friendsOfUser, user);

  if(actualUser == NULL)
    {
      actualUser = (dictionary_t*)make_dictionary(0, free);
      dictionary_set(friendsOfUser, user, actualUser);
    }

  char **actualFriends = split_string((char *)dictionary_get(query, "friends"), '\n');

  if(actualFriends == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Invalid friends!");
    }

  // For each person in the new friend's friends list, add them and add the user to their list as well
  int i;
  for(i = 0; actualFriends[i] != NULL; i++)
    {
      if(strcmp(actualFriends[i], user) != 0)
	{
	  dictionary_t* newFriends = (dictionary_t *)dictionary_get(friendsOfUser, user);

	  if(newFriends == NULL)
	    {
	      newFriends = (dictionary_t *)make_dictionary(0, free);
	      dictionary_set(friendsOfUser, user, newFriends); 
	    }

	  if(dictionary_get(newFriends, actualFriends[i]) == NULL)
	    {
	      dictionary_set(newFriends, actualFriends[i], NULL);
	    }

	  dictionary_t* newFriendsToAdd = (dictionary_t *)dictionary_get(friendsOfUser, actualFriends[i]);

	  if(newFriendsToAdd == NULL)
	    {
	      newFriendsToAdd = (dictionary_t *)make_dictionary(0, free);
	      dictionary_set(friendsOfUser, actualFriends[i], newFriendsToAdd);
	    }

	  if(dictionary_get(newFriendsToAdd, user) == NULL)
	    {
	      dictionary_set(newFriendsToAdd, user, NULL);
	    }
	}
    }

  // Now we can use the updated list of friends
  actualUser = (dictionary_t *)dictionary_get(friendsOfUser, user);
  const char** names = dictionary_keys(actualUser);
  body = join_strings(names, '\n');

  len = strlen(body);

  // Cleanup
  header = ok_header(len, "text/http; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  Rio_writen(fd, body, len);
}

/*
 * Handles the unfriend request, which allows a user to take themselves off another user's list, and vice versa.
 */
static void serve_requestUnfriend(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  if(dictionary_count(query) != 2)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Not enough arguments for the query!");
    }

  const char* user = (char *)dictionary_get(query, "user");

  if(user == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "User is invalid/does not exist!");
    }

  dictionary_t* userFriends = (dictionary_t *)dictionary_get(friendsOfUser, user);

  if(userFriends == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "This user does not exist!");
    }

  char** listToRemove = split_string((char *)dictionary_get(query, "friends"), '\n');

  if(listToRemove == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "This user doesn't have any friends!");
    }

  // For each person to remove, removes the user from that person's list
  int i;
  for(i = 0; listToRemove[i] != NULL; i++)
    {
      dictionary_remove(userFriends, listToRemove[i]);
      dictionary_t* friendsList = (dictionary_t *)dictionary_get(friendsOfUser, listToRemove[i]);

      if(friendsList != NULL)
	{
	  dictionary_remove(friendsList, user);
	}
    }

  // Now we can use the updated friends list
  userFriends = (dictionary_t *)dictionary_get(friendsOfUser, user);
  const char** names = dictionary_keys(userFriends);
  body = join_strings(names, '\n');

  len = strlen(body);
  // Cleanup
  header = ok_header(len, "text/html; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  Rio_writen(fd, body, len);
}

/*
 * Handles the introduce request, which allows cross-server communication for the purpose of adding 1. friend
 * and 2. all of friend's friends to user, and vice versa.
 */
static void serve_requestIntroduce(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  if(dictionary_count(query) != 4)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Not enough arguments for the query!");
      return;
    }

  // Collect all necessary data from the query
  const char* user = dictionary_get(query, "user");
  const char* friend = dictionary_get(query, "friend");
  char* host = (char *)dictionary_get(query, "host");
  char* port = (char *)dictionary_get(query, "port");

  if(user == NULL || friend == NULL || host == NULL || port == NULL)
    {
      clienterror(fd, "POST", "400", "Bad Request", "One or more of the query arguments are invalid!");
      return;
    }

  body = "";

  // Creates the connection to the other server if necessary, writes necessary data into a buffer
  char firstBuffer[MAXBUF];
  int connectionFD = Open_clientfd(host, port);
  sprintf(firstBuffer, "GET /friends?user=%s HTTP/1.1\r\n\r\n", query_encode(friend));
  Rio_writen(connectionFD, firstBuffer, strlen(firstBuffer));
  Shutdown(connectionFD, SHUT_WR);

  // Reads from the connection into another buffer
  char secondBuffer[MAXLINE];
  rio_t RIO;
  Rio_readinitb(&RIO, connectionFD);

  if(Rio_readlineb(&RIO, secondBuffer, MAXLINE) <= 0)
    {
      clienterror(fd, "POST", "400", "Bad Request", "Unable to read from the connection!");
    }

  char* version;
  char* status;
  char* description;

  // Checks the connection's status: if anything's not right, report
  if(!parse_status_line(secondBuffer, &version, &status, &description))
    {
      clienterror(fd, "GET", "400", "Bad Request", "No status returned!");
    }

  else
    {
      if(strcasecmp(version, "HTTP/1.0") && strcasecmp(version, "HTTP/1.1"))
	{
	  clienterror(fd, version, "501", "No Method", "The server does not have the required method!");
	}

      else if(strcasecmp(status, "200") && strcasecmp(description, "OK"))
	{
	  clienterror(fd, status, "501", "No Method", "Received a status other than OK!");
	}

      else
	{
	  // If all clear, get the request headers from the connection to use
	  dictionary_t* requestHeaders = read_requesthdrs(&RIO);
	  char* length = dictionary_get(requestHeaders, "Content-length");
	  int actualLength = (length ? atoi(length) : 0);
	  char buffer[actualLength];

	  if(actualLength <= 0)
	    {
	      clienterror(fd, "GET", "400", "Bad Request", "Nothing was received from the connection!");
	    }

	  else
	    {
	      // Reads a list of new friends from the connection into a buffer
	      Rio_readnb(&RIO, buffer, actualLength);
	      buffer[actualLength] = 0;
	      // THREAD LOCKS HERE!
	      pthread_mutex_lock(&mainThread);

	      dictionary_t* userFriends = dictionary_get(friendsOfUser, user);
	      
	      if(userFriends == NULL)
		{
		  userFriends = make_dictionary(0, NULL);
		  dictionary_set(friendsOfUser, user, userFriends);
		}

	      char** newFriends = split_string(buffer, '\n');

	      // For each user in the new list, get them and their friends added, and vice versa 
	      int i;
	      for(i = 0; newFriends[i] != NULL; i++)
		{
		  if(strcmp(newFriends[i], user) != 0)
		    {
		      // Add the target to the list and vice versa
		      dictionary_t* firstFriends = (dictionary_t *)dictionary_get(friendsOfUser, user);

		      if(firstFriends == NULL)
			{
			  firstFriends = (dictionary_t *)make_dictionary(0, free);
			  dictionary_set(friendsOfUser, user, firstFriends);
			}

		      if(dictionary_get(firstFriends, newFriends[i]) == NULL)
			{
			  dictionary_set(firstFriends, newFriends[i], NULL);
			}
		      // Now add the target's friends and vice versa
		      dictionary_t* secondFriends = (dictionary_t *)dictionary_get(friendsOfUser, newFriends[i]);

		      if(secondFriends == NULL)
			{
			  secondFriends = (dictionary_t *)make_dictionary(0, free);
			  dictionary_set(friendsOfUser, newFriends[i], secondFriends);
			}

		      if(dictionary_get(secondFriends, user) == NULL)
			{
			  dictionary_set(secondFriends, user, NULL);
			}

		      free(newFriends[i]);
		    }
		}

	      free(newFriends);
	      // Now we can use the updated friends list
	      const char** names = dictionary_keys(userFriends);
	      body = join_strings(names, '\n');

	      // THREAD UNLOCKS HERE!
	      pthread_mutex_unlock(&mainThread);

	      len = strlen(body);
	      // Cleanup
	      header = ok_header(len, "text/html; charset=utf-8");
	      Rio_writen(fd, header, strlen(header));
	      printf("Response headers:\n");
	      printf("%s", header);

	      free(header);

	      Rio_writen(fd, body, len);

	      free(body);
	    }
	}
      free(version);
      free(status);
      free(description);
    }
  // Close the connection properly!
  Close(connectionFD);
}

/*
 * clienterror - returns an error message to the client
 */
void clienterror(int fd, char *cause, char *errnum, 
		 char *shortmsg, char *longmsg) {
  size_t len;
  char *header, *body, *len_str;

  body = append_strings("<html><title>Friendlist Error</title>",
                        "<body bgcolor=""ffffff"">\r\n",
                        errnum, " ", shortmsg,
                        "<p>", longmsg, ": ", cause,
                        "<hr><em>Friendlist Server</em>\r\n",
                        NULL);
  len = strlen(body);

  /* Print the HTTP response */
  header = append_strings("HTTP/1.0 ", errnum, " ", shortmsg, "\r\n",
                          "Content-type: text/html; charset=utf-8\r\n",
                          "Content-length: ", len_str = to_string(len), "\r\n\r\n",
                          NULL);
  free(len_str);
  
  Rio_writen(fd, header, strlen(header));
  Rio_writen(fd, body, len);

  free(header);
  free(body);
}

static void print_stringdictionary(dictionary_t *d) {
  int i, count;

  count = dictionary_count(d);
  for (i = 0; i < count; i++) {
    printf("%s=%s\n",
           dictionary_key(d, i),
           (const char *)dictionary_value(d, i));
  }
  printf("\n");
}

/*
 * Handles the creation of a new process thread.  
 */
void *threadCreation(void *connectionFDP)
{
  int connectionFDPInt = *(int *)connectionFDP;
  // Frees the initial pointer, sends the int representation to the doit() method,
  // then closes it to stop memory leaks
  free(connectionFDP);
  doit(connectionFDPInt);
  Close(connectionFDPInt);

  return NULL;
}
