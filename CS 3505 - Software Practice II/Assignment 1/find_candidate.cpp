/* Brandon Walters
 * January 9th, 2020
 * CS 3505 - Peter Jensen
 */

// Inclusion of the point vector for representing points
#include "pntvec.h"
#include <iostream>
// Inclusion for reading from files
#include <fstream>
#include <sstream>
// Inclusion for square roots
#include <cmath>
// Inclusion for storing the points read from the files
#include <vector>


// Forward function declarations
std::vector<double> score_candidate(std::vector<pntvec> point, std::vector<pntvec> candidate);
std::vector<pntvec> create_point_vector();
std::vector<pntvec> create_candidate_vector();
double calculate_distance(pntvec point, pntvec candidate);
int find_minimum_score_position(std::vector<double> scores);
double find_minimum_score(std::vector<double> scores);

/*
 * Application entry point
 * 
 * Creates vectors to hold all the points in the point cloud, all of the candidate points,
 * and all of the scores for each candidate point. Uses helper methods to read each of the files
 * and fill these vectors. Then, use a method to find the minimum score and use that to find the
 * associated candidate point. Print these to the console.
 */
int main()
 {
  std::vector<pntvec> points;
  std::vector<pntvec> candidates;
  std::vector<double> scores;

  // Fills the vectors using helper methods
  points = create_point_vector();
  
  candidates = create_candidate_vector();

  scores = score_candidate(points, candidates);
  
  pntvec best_candidate;
  double best_candidate_score;

  // Uses helper methods to find the best candidate point and its score
  best_candidate = candidates[find_minimum_score_position(scores)];
  best_candidate_score = find_minimum_score(scores);

  // Prints the best candidate point and its score
  std::cout << best_candidate.x << " " << best_candidate.y << " " << best_candidate.z << std::endl;
  std::cout << best_candidate_score << std::endl;

  return 0;
}

/*
 * For each candidate point, uses a helper method to go through all the points and score them
 * to determine which candidate point is the best.
 */
std::vector<double> score_candidate(std::vector<pntvec> point, std::vector<pntvec> candidate) 
{
  std::vector<double> scores;

  // Loops through each candidate to score
  for (int i = 0; i < candidate.size(); i++) 
  {
    double score;
    pntvec candidate_vector;

    candidate_vector = candidate[i];
    
    // Gets each point and adds up the score
    for (int j = 0; j < point.size(); j++) 
    {
      pntvec point_vector;
      point_vector = point[j];
      
      score = score + pow(calculate_distance(point_vector, candidate_vector), 2);
    }

    // Adds the new score to the scores vector, resets the score for the next candidate
    scores.push_back(score);
    score = 0;
  }
  return scores;
}

/*
 * Finds the vector position of the minimum score, which corresponds with the position of the best candidate point.
 */
int find_minimum_score_position(std::vector<double> scores) 
{
  double min;
  int min_position;
  min = scores[0];
  min_position = 0;

  // Scans each score to find the minimum score, which will give the correct position for the best candidate point
  for (int i = 0; i < scores.size(); i++) 
  {
    if (scores[i] < min) 
    {
      min = scores[i];
      min_position = i;
    }
  }
  return min_position;
}

/*
 * Finds the minimum score in the scores vector
 */
double find_minimum_score(std::vector<double> scores) 
{
  double min;
  min = scores[0];

  // Scans each score to find the minimum score
  for (int i = 0; i < scores.size(); i++) 
  {
    if (scores[i] < min) 
    {
      min = scores[i];
    }
  }
  return min;
}

/*
 * Used to calculate the distance between a given candidate point and each point in the cloud
 */
double calculate_distance(pntvec point, pntvec candidate) 
{
  return sqrt(pow(candidate.x - point.x, 2) + pow(candidate.y - point.y, 2) + pow(candidate.z - point.z, 2));
}

/*
 * Reads the point_cloud file to fill the points vector with a list of points
 */
std::vector<pntvec> create_point_vector() 
{
  // Writes the point cloud file
  std::ifstream points_file("point_cloud.txt");

  std::vector<pntvec> points;
  pntvec point_vector;

  double point_x;
  double point_y;
  double point_z;
  // While there is a point in the file, reads the point and adds it to the vector of points
  while (points_file >> point_x >> point_y >> point_z) 
  {
    point_vector.x = point_x;
    point_vector.y = point_y;
    point_vector.z = point_z;

    points.push_back(point_vector);
  }
  // Closes the file and returns the vector of points
  points_file.close();
  return points;
}

/*
 * Reads the candidates file to fill the candidates vector with a list of candidate points
 */
std::vector<pntvec> create_candidate_vector() 
{
  // Writes the candidate file
  std::ifstream candidates_file("candidates.txt");
  
  std::vector<pntvec> candidates;
  pntvec candidate;

  double point_x, point_y, point_z;
  
  // While there is a candidate point left in the file, reads it and adds it to the candidate points vector
  while (candidates_file >> point_x >> point_y >> point_z) 
  {
    candidate.x = point_x;
    candidate.y = point_y;
    candidate.z = point_z;

    candidates.push_back(candidate);
  }
  // Closes the file and returns the vector of candidates
  candidates_file.close();
  return candidates;
}
