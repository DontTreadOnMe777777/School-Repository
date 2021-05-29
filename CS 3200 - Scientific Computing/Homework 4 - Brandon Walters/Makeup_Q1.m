xArrayGelbTanner = linspace(-1,1,100);
xArrayHubbard = linspace(0,1,100);

gelbTannerArray = GelbTanner(xArrayGelbTanner);
hubbardArray = hubbard(xArrayHubbard);

figure
plot(xArrayGelbTanner, gelbTannerArray(:));
title('Gelb-Tanner on (-1, 1) Interval, 100 Points');

figure
plot(xArrayHubbard, hubbardArray(:));
title('Hubbard on (0, 1) Interval, 100 Points');