// bgImg is the background image to be modified.
// fgImg is the foreground image.
// fgOpac is the opacity of the foreground image.
// fgPos is the position of the foreground image in pixels. It can be negative and (0,0) means the top-left pixels of the foreground and background are aligned.

// Written by Brandon Walters, 9/8/2020
function composite( bgImg, fgImg, fgOpac, fgPos )
{
    // First, we need to find the overlap between our foreground and our background images. We can calculate our overlap using fgPos
    // coordinates and the height/width of both pictures.
    let overlapLeft = fgPos.x;
    let overlapTop = fgPos.y;
    let overlapRight = overlapLeft + fgImg.width;
    let overlapBottom = overlapTop + fgImg.height;

    // If the images are the same size (every pixel overlaps), use the simple algorithm.
    if(bgImg.width == fgImg.width && bgImg.height == fgImg.height)
    {
        for(let i = 0; i < bgImg.data.length; i += 4)
        {
            // If the pixel is not transparent, modify the pixel
            if ((fgImg.data[i + 3] / 255.0) != 0.0)
            {
                bgImg.data[i] = (fgImg.data[i] * fgOpac + bgImg.data[i] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
                bgImg.data[i + 1] = (fgImg.data[i + 1] * fgOpac + bgImg.data[i + 1] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
                bgImg.data[i + 2] = (fgImg.data[i + 2] * fgOpac + bgImg.data[i + 2] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
            }
        }
    }

    // If the two images are not the same size, use the foreground/background pixel tracking
    else
        {
        let bgRowNum = 0;
        let bgColNum = 0;
        let fgRowNum = 0;
        let fgColNum = 0;
        let fgOverlapped = false;

        // If there is a negative overlap, change the starting position of the foreground image accordingly
            if (fgPos.x < 0)
            {
                fgColNum = -fgPos.x;
            }

            if (fgPos.y < 0)
            {
                fgRowNum = -fgPos.y;
            }

        // Keeps track of the rows
        for (let j = 0; j < bgImg.height; j++) {
            // Keeps track of the columns
            for (let i = 0; i < bgImg.width * 4; i += 4) {
                // If the foreground image overlaps the background image at the specified column and row, continue
                if ((overlapLeft <= bgColNum) && (bgColNum < overlapRight) && (overlapTop <= bgRowNum) && (bgRowNum < overlapBottom)) {
                    // If the foreground pixel is not transparent, modify the pixel
                    if ((fgImg.data[((fgRowNum * (fgImg.width * 4)) + (fgColNum * 4)) + 3] / 255.0) != 0.0) {
                        bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4))] = (fgImg.data[((fgRowNum * (fgImg.width * 4)) + (fgColNum * 4))] * fgOpac + bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4))] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
                        bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4)) + 1] = (fgImg.data[((fgRowNum * (fgImg.width * 4)) + (fgColNum * 4)) + 1] * fgOpac + bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4)) + 1] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
                        bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4)) + 2] = (fgImg.data[((fgRowNum * (fgImg.width * 4)) + (fgColNum * 4)) + 2] * fgOpac + bgImg.data[((bgRowNum * (bgImg.width * 4)) + (bgColNum * 4)) + 2] * (1 - fgOpac)) / (fgOpac + (1 - fgOpac));
                    }
                    fgColNum++;
                    fgOverlapped = true;
                }
                bgColNum++;
            }
            bgRowNum++;
            if (fgOverlapped) {
                fgRowNum++;
            }
            bgColNum = 0;
            fgColNum = 0;
        }
    }
}
