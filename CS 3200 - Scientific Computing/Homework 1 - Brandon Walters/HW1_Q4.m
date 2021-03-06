% Arrays to hold the proper time, the specific n value, and the value generated by 
% the model equation are declared here.
timeArray = NaN(1,100);
nArray = NaN(1,100);
equationArray = NaN(1,100);

% This for loop contains the code necessary to run our equation and collect
% our data.
for i = 1:100
    % Create our proper large n value, stores that value into the nArray
    n = i*10;
    nArray(i) = n;
    % Generate two random identity matrices
    a = eye(n,n);
    b = eye(n,n);
    % We now modify the identity matrices to include the adjacent negative
    % values using another for loop
    for j = 2:n
        a(j-1,j) = -0.5;
        a(j,j-1) = -0.5;
        b(j-1,j) = -0.5;
        b(j,j-1) = -0.5;
    end
    tic
    % Now we time our solve operation
    c = a\b;
    % Store the time into the proper array
    timeArray(i) = toc;
    % Finally, calculate the model equation and store that value
    equationArray(i) = 50*(n/1000)^1;
end

% Now we plot our times properly: The first two use our proper time values,
% the second pair use our model equation values.
plot(nArray,timeArray);
figure
semilogy(nArray,timeArray);

figure
plot(equationArray,timeArray);
figure
semilogy(equationArray,timeArray);