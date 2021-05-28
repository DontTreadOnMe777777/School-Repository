% First integral
itr = 1;
for i = 3:8
    npts  =  2^(i+1)+1; nptsm1=npts-1; 

    x = linspace(0,1,npts);
    h = 1/(npts-1); %spacing of intervals 
    Simpint(i-2)= 0.0;
    
    for p = [2 3 4 5 6 8]
        for ii =1:nptsm1 % calculate estimate of integral
            xmid= (x(ii)+x(ii+1))*0.5;  
            Simpint(i-2)  =  Simpint(i-2)+ h*((x(ii)^p) + (x(ii+1)^p)+ 4.0*(xmid)^p)/6.0;
        end
        finalSimp(itr)= Simpint(i-2);
        itr = itr + 1;
    end
end


% Second integral
itr = 1;
for i = 3:8
    npts  =  2^(i+1)+1; nptsm1=npts-1; 

    x = linspace(0,1,npts);
    h = 2*pi/(npts-1); %spacing of intervals 
    Simpint(i-2)= 0.0;
    
    %for p = [2 3 4 5 6 8]
        tic
        for ii =1:nptsm1 % calculate estimate of integral
            xmid= (x(ii)+x(ii+1))*0.5;  
            Simpint(i-2)  =  Simpint(i-2)+ h*((1 + sin(x(ii)) * cos(2*x(ii)/3) * sin(4*x(ii))) + (1 + sin(x(ii+1)) * cos(2*x(ii+1)/3) * sin(4*x(ii+1)))+ 4.0*(1 + sin(xmid) * cos(2*xmid/3) * sin(4*xmid)))/6.0;
        end
        timingArray(itr) = toc; %Save timing of Simpson step
        finalSimpSecond(itr)= Simpint(i-2);
        itr = itr + 1;
    %end
end

% Table for both integrals
finalSimp = finalSimp(:);
pArray = [2 3 4 5 6 8];
T = table(pArray(:), finalSimp([1 2 3 4 5 6], 1), finalSimp([7 8 9 10 11 12], 1), finalSimp([13 14 15 16 17 18], 1), finalSimp([19 20 21 22 23 24], 1), finalSimp([25 26 27 28 29 30], 1), finalSimp([31 32 33 34 35 36], 1));
uitable('Data',T{:,:},'ColumnName',{'Power', '17 Points', '33 Points' , '65 Points', '129 Points', '257 Points', '513 Points'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);

pointsArray = [17 33 65 129 257 513];
figure
T = table(pointsArray(:), finalSimpSecond(:), timingArray(:));
uitable('Data',T{:,:},'ColumnName',{'Number of Points/Iterations', 'Common Value of Convergence', 'Time to Converge'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);