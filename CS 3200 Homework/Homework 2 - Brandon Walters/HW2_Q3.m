xArray = linspace(0,2,1001);

vanderXArray6 = linspace(0,2,6);
vanderXArray11 = linspace(0,2,11);
vanderXArray21 = linspace(0,2,21);
vanderXArray41 = linspace(0,2,41);
vanderXArray81 = linspace(0,2,81);
vanderXArray161 = linspace(0,2,161);
vanderXArray321 = linspace(0,2,321);
vanderXArray641 = linspace(0,2,641);

x = linspace(0,2,3)';
expx  = exp(x);
A = fliplr(vander(x));
a = A\expx;

yplot6 = ones(1,6)*a(3); 
yplot11 = ones(1,11)*a(3); 
yplot21 = ones(1,21)*a(3); 
yplot41 = ones(1,41)*a(3); 
yplot81 = ones(1,81)*a(3); 
yplot161 = ones(1,161)*a(3); 
yplot321 = ones(1,321)*a(3);
yplot641 = ones(1,641)*a(3); % calculate polynomial at plotting points 
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


chebyshevX6 = ones(1,6);  
for k = 1:6       
    chebyshevX6(k) =  -cos(pi*(k-1.0)/(6.0-1.0)) + 1;        
end

chebyshevX11 = ones(1,11);   
for k = 1:11       
    chebyshevX11(k) =  -cos(pi*(k-1.0)/(11.0-1.0)) + 1;        
end

chebyshevX21 = ones(1,21);   
for k = 1:21       
    chebyshevX21(k) =  -cos(pi*(k-1.0)/(21.0-1.0)) + 1;        
end

chebyshevX41 = ones(1,41);  
for k = 1:41       
    chebyshevX41(k) =  -cos(pi*(k-1.0)/(41.0-1.0)) + 1;        
end

chebyshevX81 = ones(1,81);   
for k = 1:81       
    chebyshevX81(k) =  -cos(pi*(k-1.0)/(81.0-1.0)) + 1;        
end

chebyshevX161 = ones(1,161);   
for k = 1:161       
    chebyshevX161(k) =  -cos(pi*(k-1.0)/(161.0-1.0)) + 1;        
end

chebyshevX321 = ones(1,321);  
for k = 1:321       
    chebyshevX321(k) =  -cos(pi*(k-1.0)/(321.0-1.0)) + 1;        
end

chebyshevX641 = ones(1,641);  
for k = 1:641       
    chebyshevX641(k) =  -cos(pi*(k-1.0)/(641.0-1.0)) + 1;        
end

chebyshevXFull = ones(1,1001);   
for k = 1:1001       
    chebyshevXFull(k) =  -cos(pi*(k-1.0)/(1001.0-1.0)) + 1;        
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

exponentialArray = exp(xArray);
exponentialArrayChebyshev = exp(chebyshevXFull);


% Polyinterp calculations
poly6Linear = polyinterp(vanderXArray6, yplot6, xArray);
poly11Linear = polyinterp(vanderXArray11, yplot11, xArray);
poly21Linear = polyinterp(vanderXArray21, yplot21, xArray);
poly41Linear = polyinterp(vanderXArray41, yplot41, xArray);
poly81Linear = polyinterp(vanderXArray81, yplot81, xArray);
poly161Linear = polyinterp(vanderXArray161, yplot161, xArray);
poly321Linear = polyinterp(vanderXArray321, yplot321, xArray);
poly641Linear = polyinterp(vanderXArray641, yplot641, xArray);

poly6Chebyshev = polyinterp(chebyshevX6, yplot6Chebyshev, chebyshevXFull);
poly11Chebyshev = polyinterp(chebyshevX11, yplot11Chebyshev, chebyshevXFull);
poly21Chebyshev = polyinterp(chebyshevX21, yplot21Chebyshev, chebyshevXFull);
poly41Chebyshev = polyinterp(chebyshevX41, yplot41Chebyshev, chebyshevXFull);
poly81Chebyshev = polyinterp(chebyshevX81, yplot81Chebyshev, chebyshevXFull);
poly161Chebyshev = polyinterp(chebyshevX161, yplot161Chebyshev, chebyshevXFull);
poly321Chebyshev = polyinterp(chebyshevX321, yplot321Chebyshev, chebyshevXFull);
poly641Chebyshev = polyinterp(chebyshevX641, yplot641Chebyshev, chebyshevXFull);



% Barylag calculations, use colon to force column vectors and transpose
% xArray
bary6Linear = barylag([vanderXArray6(:), yplot6(:)], xArray');
bary11Linear = barylag([vanderXArray11(:), yplot11(:)], xArray');
bary21Linear = barylag([vanderXArray21(:), yplot21(:)], xArray');
bary41Linear = barylag([vanderXArray41(:), yplot41(:)], xArray');
bary81Linear = barylag([vanderXArray81(:), yplot81(:)], xArray');
bary161Linear = barylag([vanderXArray161(:), yplot161(:)], xArray');
bary321Linear = barylag([vanderXArray321(:), yplot321(:)], xArray');
bary641Linear = barylag([vanderXArray641(:), yplot641(:)], xArray');

bary6Chebyshev = barylag([chebyshevX6(:), yplot6Chebyshev(:)], chebyshevXFull');
bary11Chebyshev = barylag([chebyshevX11(:), yplot11Chebyshev(:)], chebyshevXFull');
bary21Chebyshev = barylag([chebyshevX21(:), yplot21Chebyshev(:)], chebyshevXFull');
bary41Chebyshev = barylag([chebyshevX41(:), yplot41Chebyshev(:)], chebyshevXFull');
bary81Chebyshev = barylag([chebyshevX81(:), yplot81Chebyshev(:)], chebyshevXFull');
bary161Chebyshev = barylag([chebyshevX161(:), yplot161Chebyshev(:)], chebyshevXFull');
bary321Chebyshev = barylag([chebyshevX321(:), yplot321Chebyshev(:)], chebyshevXFull');
bary641Chebyshev = barylag([chebyshevX641(:), yplot641Chebyshev(:)], chebyshevXFull');



% Error calc
sqrth = 1.0/sqrt(1001);
errorLinear = norm((yplotFull-exponentialArray),inf); % max value of error at points 
err2Linear = sqrth*norm((yplotFull-exponentialArray),2);

errorChebyshev = norm((yplotFullChebyshev-exponentialArrayChebyshev),inf); % max value of error at points 
err2Chebyshev = sqrth*norm((yplotFullChebyshev-exponentialArrayChebyshev),2);

fprintf('Linear infinity error = %8.2e \nChebyshev infinity error = %8.2e\n\n', errorLinear, errorChebyshev);
fprintf('Linear 2-Norm error = %8.2e \nChebyshev 2-Norm error = %8.2e\n\n', err2Linear, err2Chebyshev);

% Plot data, including the two new functions
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

figure
xlim([0 2])
subplot(1,2,1)
plot(xArray, exponentialArray, xArray, poly6Linear, chebyshevXFull, poly81Chebyshev);
legend(' True values',' Linear Poly', ' Chebyshev Poly');
title('Polyinterp');

subplot(1,2,2)
plot(xArray, exponentialArray, xArray, bary21Linear, chebyshevXFull, bary641Chebyshev);
legend(' True values',' Linear Bary', ' Chebyshev Bary');
title('Barylag');