% Setting up storage arrays for our norms
normXArray = zeros(6, 1);
normXArrayItr = 1;

normXInvArray = zeros(6, 1);
normXInvArrayItr = 1;

normXInvFilterArray = zeros(24, 1);
normXInvFilterArrayItr = 1;

normMatricesArray = zeros(8, 1);
normMatricesArrayItr = 1;

% Arrays for tables
iArray = [5 10 15 20 25 30];
tolArray = [1e-10 1e-12 1e-14 1e-16];

for i = [5 10 15 20 25 30]
    hilbertMatrix = hilb(i);
    x = ones(i, 1);
    
    b = hilbertMatrix * x;
    actualX = hilbertMatrix\b; % Linear solve method
    
    normX = norm(x - actualX,2);
    normXArray(normXArrayItr) = normX;
    normXArrayItr = normXArrayItr + 1;
    
    % Generate SVD, get inverse of S
    [U, S, V] = svd(hilbertMatrix); 
    
    SInverse = inv(S);
    
    hilbertMatrixInverse = V * SInverse * U';
    xFromInverse = hilbertMatrixInverse * b; % SVD inverse solve method
    
    normXFromInverse = norm(x - xFromInverse,2);
    normXInvArray(normXInvArrayItr) = normXFromInverse;
    normXInvArrayItr = normXInvArrayItr + 1;
    
    for tol = [1e-10 1e-12 1e-14 1e-16]
        SFiltered = S;
        SFilteredInv = SInverse;
        
        % Filter out values below the tolerance from both matrices
        for j = 1:i 
           if SFiltered(j,j) < tol
              SFiltered(j,j) = 0;
              SFilteredInv(j,j) = 0;
           end
        end
        
        hilbInvFiltered = V * SFilteredInv * U';
        xInvFiltered = hilbInvFiltered * b; % SVD filtered inverse solve method
        
        normXFiltered = norm(x - xInvFiltered,2);
        normXInvFilterArray(normXInvFilterArrayItr) = normXFiltered;
        normXInvFilterArrayItr = normXInvFilterArrayItr + 1;
        
        reconHilb = V * SFiltered * U'; % Reconstructed Hilb using filtered S
        if i == 5 || i == 10
            disp('Original Hilbert matrix:');
            disp(hilbertMatrix);
            disp('Filtered Hilbert matrix:');
            disp(reconHilb);
        
            normMatrices = norm(hilbertMatrix - reconHilb,2);
            normMatricesArray(normMatricesArrayItr) = normMatrices;
            normMatricesArrayItr = normMatricesArrayItr + 1;
            
            fprintf('Size of Hilbert Matrix - %i Current Tolerance - %8.2e 2 Norm Error - %8.2e\n', i,tol,normMatrices)
        end 
    end
end

% Create tables for data
T = table(iArray(:), normXArray(:), normXInvArray(:));
uitable('Data',T{:,:},'ColumnName',{'Hilbert Matrix Size', '\ Method Norm', 'SVD Inverse Norm'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);

figure
T = table(tolArray(:), normXInvFilterArray([1 2 3 4], 1),  normXInvFilterArray([5 6 7 8], 1),  normXInvFilterArray([9 10 11 12], 1),  normXInvFilterArray([13 14 15 16], 1), normXInvFilterArray([17 18 19 20], 1), normXInvFilterArray([21 22 23 24], 1));
uitable('Data',T{:,:},'ColumnName',{'Tolerances', '5x5 Hilbert Matrix', '10x10 Hilbert Matrix' , '15x15 Hilbert Matrix', '20x20 Hilbert Matrix', '25x25 Hilbert Matrix', '30x30 Hilbert Matrix'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);