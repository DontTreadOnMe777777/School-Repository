% Arrays for alpha, condition number, and constant values
aArray = [1.0e-1, 1.0e-3, 1.0e-5, 1.0e-7, 1.0e-9, 1.0e-11, 1.0e-13, 1.0e-15];
condArray = zeros(1,32);
condArrIndex = 1;
constArray = zeros(1,32);
constArrIndex = 1;
% Arrays for each residual iteration step
normb1 = zeros(1,32);
normb2 = zeros(1,32);
normb3 = zeros(1,32);
norm1ArrIndex = 1;
norm2ArrIndex = 1;
norm3ArrIndex = 1;

for i = [21 41 81 161]
    hArray = zeros(1,i);
    hArray(1) = -8;
    for j = 1:8   
        % Pick our specific alpha and create our matrix
        a = aArray(j);
        hArray(i)= 4 * -a;
        % Create vectors for the first half, second half, and the middle
        % value for each diagonal
        vectorMidBegin = ones(1,fix(i/2)) * -2;
        vectorMidMiddle = ones(1,1) * -(1 + a);
        vectorMidEnd = ones(1,fix(i/2)) * -2 * a;
        
        vectorMidFull = [vectorMidBegin,vectorMidMiddle,vectorMidEnd];
        
        vectorLowerBegin = ones(1,fix(i/2) - 1);
        vectorLowerMiddle = ones(1,1);
        vectorLowerEnd = ones(1,fix(i/2)) * a;
        
        vectorLowerFull = [vectorLowerBegin,vectorLowerMiddle,vectorLowerEnd];
        
        vectorHigherBegin = ones(1,fix(i/2));
        vectorHigherMiddle = ones(1,1) * a;
        vectorHigherEnd = ones(1,fix(i/2) - 1) * a;
        
        vectorHigherFull = [vectorHigherBegin,vectorHigherMiddle,vectorHigherEnd];
        % Combine the three diagonal vectors into a full matrix
        matrix = diag(vectorMidFull) + diag(vectorLowerFull, -1) + diag(vectorHigherFull, 1);
        
        
        % Get the condition number and store it, then calculate the
        % constant using the given formula and store that
        condArray(condArrIndex) = cond(matrix);
        constArray(constArrIndex) = 1/(condArray(condArrIndex) * a);
        
        condArrIndex = condArrIndex + 1;
        constArrIndex = constArrIndex + 1;
        
        
        % Begin residual calculations
        x = sym(matrix)\sym(hArray(:));
        r = matrix*x;
        
        for p = 1:3
            % Store each step in the correct residual spot
        if p == 1
            normb1(norm1ArrIndex) = norm(r,inf);
            norm1ArrIndex = norm1ArrIndex + 1;
        end
        if p == 2
            normb2(norm2ArrIndex) = norm(r,inf);
            norm2ArrIndex = norm2ArrIndex + 1;
        end
        if p == 3
            normb3(norm3ArrIndex) = norm(r,inf);
            norm3ArrIndex = norm3ArrIndex + 1;
        end
        % Calculate next residual
        d = matrix\r;
        x = x - d;
        
        r = matrix*x;
        end
    end
end

% Create table of data
T = table(condArray(:), constArray(:), normb1(:), normb2(:), normb3(:));

uitable('Data',T{:,:},'ColumnName',{'Condition Number', 'Constant for Cond Calc', 'Original Residual', 'First Iteration for Refinement', 'Second Iteration for Refinement'},...
    'RowName',{'Matrix Size = 21, a = 1.0e-1', 'Matrix Size = 21, a = 1.0e-3', 'Matrix Size = 21, a = 1.0e-5', 'Matrix Size = 21, a = 1.0e-7', 'Matrix Size = 21, a = 1.0e-9', 'Matrix Size = 21, a = 1.0e-11', 'Matrix Size = 21, a = 1.0e-13', 'Matrix Size = 21, a = 1.0e-15', 'Matrix Size = 41, a = 1.0e-1', 'Matrix Size = 41, a = 1.0e-3', 'Matrix Size = 41, a = 1.0e-5', 'Matrix Size = 41, a = 1.0e-7', 'Matrix Size = 41, a = 1.0e-9', 'Matrix Size = 41, a = 1.0e-11', 'Matrix Size = 41, a = 1.0e-13', 'Matrix Size = 41, a = 1.0e-15', 'Matrix Size = 81, a = 1.0e-1', 'Matrix Size = 81, a = 1.0e-3', 'Matrix Size = 81, a = 1.0e-5', 'Matrix Size = 81, a = 1.0e-7', 'Matrix Size = 81, a = 1.0e-9', 'Matrix Size = 81, a = 1.0e-11', 'Matrix Size = 81, a = 1.0e-13', 'Matrix Size = 81, a = 1.0e-15', 'Matrix Size = 161, a = 1.0e-1', 'Matrix Size = 161, a = 1.0e-3', 'Matrix Size = 161, a = 1.0e-5', 'Matrix Size = 161, a = 1.0e-7', 'Matrix Size = 161, a = 1.0e-9', 'Matrix Size = 161, a = 1.0e-11', 'Matrix Size = 161, a = 1.0e-13', 'Matrix Size = 161, a = 1.0e-15'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);