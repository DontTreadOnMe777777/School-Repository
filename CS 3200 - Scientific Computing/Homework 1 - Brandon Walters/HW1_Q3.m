% Arrays to hold the proper time, the specific n value, and the value generated by 
% the model equation are declared here.
timeArray = NaN(1,100);
nArray = NaN(1,100);
equationArray = NaN(1,100);

% This for loop contains the code necessary to run our equation and collect
% our data.
for i = 1:100
    % Create our proper large n value, stores that value into the nArray
    n = i*100;
    nArray(i) = n;
    % Generate a random matrix and a random vector, then time our solve operation
    a = rand(n,n);
    b = rand(n,1);
    tic
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