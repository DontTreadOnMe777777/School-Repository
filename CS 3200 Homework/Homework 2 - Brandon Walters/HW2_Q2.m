xArray = linspace(0,2,1001);

% Create linearly spaced arrays
vanderXArray6 = linspace(0,2,6);
vanderXArray11 = linspace(0,2,11);
vanderXArray21 = linspace(0,2,21);
vanderXArray41 = linspace(0,2,41);
vanderXArray81 = linspace(0,2,81);
vanderXArray161 = linspace(0,2,161);
vanderXArray321 = linspace(0,2,321);
vanderXArray641 = linspace(0,2,641);

% Create our Vandermonde matrix to use to find the y-coordinates
x = linspace(0,2,3)';
expx  = exp(x);
A = fliplr(vander(x));
a = A\expx;

% Calculate polynomial at plotting points 
yplot6 = ones(1,6)*a(3); 
yplot11 = ones(1,11)*a(3); 
yplot21 = ones(1,21)*a(3); 
yplot41 = ones(1,41)*a(3); 
yplot81 = ones(1,81)*a(3); 
yplot161 = ones(1,161)*a(3); 
yplot321 = ones(1,321)*a(3);
yplot641 = ones(1,641)*a(3); 
yplotFull = ones(1,1001)*a(3);
for j = 1:2   
    yplot6 = yplot6.*vanderXArray6 + a(3-j);
    yplot11 = yplot11.*vanderXArray11 + a(3-j);
    yplot21 = yplot21.*vanderXArray21 + a(3-j);
    yplot41 = yplot41.*vanderXArray41 + a(3-j);
    yplot81 = yplot81.*vanderXArray81 + a(3-j);
    yplot161 = yplot161.*vanderXArray161 + a(3-j);
    yplot321 = yplot321.*vanderXArray321 + a(3-j);
    yplot641 = yplot641.*vanderXArray641 + a(3-j);
    yplotFull = yplotFull.*xArray + a(3-j);  % note vector operations  
end

% Perform the same process for Chebyshev spaced points
chebyshevX6 = ones(1,6);  
for k = 1:6       
    chebyshevX6(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*6)) + 1;        
end

chebyshevX11 = ones(1,11);   
for k = 1:11       
    chebyshevX11(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*11)) + 1;        
end

chebyshevX21 = ones(1,21);   
for k = 1:21       
    chebyshevX21(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*21)) + 1;        
end

chebyshevX41 = ones(1,41);  
for k = 1:41       
    chebyshevX41(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*41)) + 1;        
end

chebyshevX81 = ones(1,81);   
for k = 1:81       
    chebyshevX81(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*81)) + 1;        
end

chebyshevX161 = ones(1,161);   
for k = 1:161       
    chebyshevX161(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*161)) + 1;        
end

chebyshevX321 = ones(1,321);  
for k = 1:321       
    chebyshevX321(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*321)) + 1;        
end

chebyshevX641 = ones(1,641);  
for k = 1:641       
    chebyshevX641(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*641)) + 1;        
end

chebyshevXFull = ones(1,1001);   
for k = 1:1001       
    chebyshevXFull(k) =  -cos(pi*(2.0*k-1.0)/( 2.0*1001)) + 1;        
end

yplot6Chebyshev = ones(1,6)*a(3); 
yplot11Chebyshev = ones(1,11)*a(3); 
yplot21Chebyshev = ones(1,21)*a(3); 
yplot41Chebyshev = ones(1,41)*a(3); 
yplot81Chebyshev = ones(1,81)*a(3); 
yplot161Chebyshev = ones(1,161)*a(3); 
yplot321Chebyshev = ones(1,321)*a(3);
yplot641Chebyshev = ones(1,641)*a(3); % calculate polynomial at plotting points 
yplotFullChebyshev = ones(1,1001)*a(3);
for j = 1:2   
    yplot6Chebyshev = yplot6Chebyshev.*chebyshevX6 + a(3-j);
    yplot11Chebyshev = yplot11Chebyshev.*chebyshevX11 + a(3-j);
    yplot21Chebyshev = yplot21Chebyshev.*chebyshevX21 + a(3-j);
    yplot41Chebyshev = yplot41Chebyshev.*chebyshevX41 + a(3-j);
    yplot81Chebyshev = yplot81Chebyshev.*chebyshevX81 + a(3-j);
    yplot161Chebyshev = yplot161Chebyshev.*chebyshevX161 + a(3-j);
    yplot321Chebyshev = yplot321Chebyshev.*chebyshevX321 + a(3-j);
    yplot641Chebyshev = yplot641Chebyshev.*chebyshevX641 + a(3-j);
    yplotFullChebyshev = yplotFullChebyshev.*chebyshevXFull + a(3-j);  % note vector operations  
end

% Calculate the actual answer
exponentialArray = exp(xArray);
exponentialArrayChebyshev = exp(chebyshevXFull);

% Calculate our error
sqrth = 1.0/sqrt(1001);
errorLinear = norm((yplotFull-exponentialArray),inf); % max value of error at points 
err2Linear = sqrth*norm((yplotFull-exponentialArray),2);

errorChebyshev = norm((yplotFullChebyshev-exponentialArrayChebyshev),inf); % max value of error at points 
err2Chebyshev = sqrth*norm((yplotFullChebyshev-exponentialArrayChebyshev),2);

fprintf('Linear infinity error = %8.2e \nChebyshev infinity error = %8.2e\n\n', errorLinear, errorChebyshev);
fprintf('Linear 2-Norm error = %8.2e \nChebyshev 2-Norm error = %8.2e\n\n', err2Linear, err2Chebyshev);

% Plot data
figure
xlim([0 2])
subplot(1,2,1)
plot(xArray, exponentialArray, xArray, yplotFull, vanderXArray6, yplot6, vanderXArray11, yplot11, vanderXArray21, yplot21, vanderXArray41, yplot41, vanderXArray81, yplot81, vanderXArray161, yplot161, vanderXArray321, yplot321, vanderXArray641, yplot641);
legend(' True values','1001 linear Vandermonde values',' 6 values',' 11 values',' 21 values',' 41 values',' 81 values',' 161 values',' 321 values',' 641 values');
title('Linear Vandermonde Interpolation');

subplot(1,2,2)
plot(chebyshevXFull, exponentialArrayChebyshev, chebyshevXFull, yplotFullChebyshev, chebyshevX6, yplot6Chebyshev, chebyshevX11, yplot11Chebyshev, chebyshevX21, yplot21Chebyshev, chebyshevX41, yplot41Chebyshev, chebyshevX81, yplot81Chebyshev, chebyshevX161, yplot161Chebyshev, chebyshevX321, yplot321Chebyshev, chebyshevX641, yplot641Chebyshev);
legend(' True values', ' 1001 Chebyshev Vandermonde values', ' 6 values', ' 11 values', ' 21 values', ' 41 values', ' 81 values', ' 161 values', ' 321 values', ' 641 values');
title('Chebyshev Vandermonde Interpolation');