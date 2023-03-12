# Seam-Carving
Say goodbye to the days of awkwardly cropping and scaling your images to fit your needs! With seam-carving, 
your images can now magically resize themselves pixel by pixel, maintaining the most important features of the original image.

It's like having a digital tailor that customizes your images to fit just right, without sacrificing any of the important details. 
Seam-carving works by identifying paths of pixels, both horizontally and vertically, and intelligently removing them to reduce the image size.

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.54.38%20PM.png)

It may seem like magic, but the underlying algorithm was only discovered in 2007! Since then, it's become a must-have feature in programs like Adobe Photoshop, 
making image resizing easier and more intuitive than ever before.

## Energy Calculation

Get ready to dive deep into the world of pixel importance! The first step in seam-carving is calculating the energy of each pixel, 
like a personal trainer determining which muscles need the most work.

Using the dual-gradient energy function, this program identifies areas of rapid color gradient, such as the boundary between the sea 
and sky or the edge of a surfer riding a wave. These high-energy pixels are deemed too important to remove, so the seam-carving technique carefully 
avoids them like a skilled surfer avoiding a dangerous wave.

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.55.04%20PM.png)

So get ready to flex your image-editing muscles, and let this program help you bring out the best in your photos.

## Seam Identification

Now, we're on the hunt for the elusive vertical seam of minimum total energy. This might sound like a classic case of finding the shortest path 
in an edge-weighted digraph, but hold on to your pixels because there are three important differences. 
First, instead of weights on edges, we've got weights on vertices. Second, we're looking for the shortest path from any of the top row's W pixels 
to any of the bottom row's W pixels. And third, our digraph is acyclic, meaning there's a one-way ticket from each pixel to its neighbors below. 

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.55.16%20PM.png)

As we dig deeper and deeper, we'll be using a MinPQ to keep track of the best distances found so far, and we'll be sure to avoid any seams trying to 
wrap around the image like a sneaky ninja.

## Computing the energy of a pixel

we'll be working with the dual-gradient energy function to calculate the energy of each pixel in an image. 
The energy function looks at color gradients between neighboring pixels, using the differences in red, green, and blue components to measure the 
importance of each pixel. 

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.55.57%20PM.png)

To handle pixels on the borders, the leftmost and rightmost columns are considered adjacent, and the topmost and bottommost rows are also considered 
adjacent. With this energy function, you'll be able to manipulate images like a pro!

## Vertical Seam

Picture this: You've got an image that's a bit too wide for your liking, but you don't want to lose any important features. 
So, what do you do? You call upon the mighty findVerticalSeam() method, of course! This little powerhouse returns an array that tells you which pixels 
to remove from each row of the image to achieve your desired width. It's like giving your image a little nip and tuck without sacrificing any of its pizzazz. 
Don't believe me? Check out the table below, showcasing the dual-gradient energies of a 6-by-5 image.

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.56.48%20PM.png)

## Horizontal Seam

Removing seams horizontally? No sweat for the findHorizontalSeam() method. It's like findVerticalSeam(), but instead of telling you which column to 
get rid of, it hands you an array of the rows to remove. For the 6-by-5 image, it would return { 2, 2, 1, 2, 1, 2 } because those are the row 
numbers of the pixel to be nixed for the minimum energy seam.

![Login](https://github.com/Shubhamg369/Private/blob/main/Screenshot%202023-03-12%20at%201.57.12%20PM.png)
