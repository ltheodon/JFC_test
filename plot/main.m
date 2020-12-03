clear all;
close all;

X1 = importdata("X1.txt");
X2 = importdata("X2.txt");

figure;
plot(X1(:,1),X1(:,2))
hold on;
plot(X2(:,1),X2(:,2))


XY = importdata("XY.txt");

figure;
%plot3(XY(:,1),XY(:,2),XY(:,3),'.','MarkerSize',15)
scatter3(XY(:,1),XY(:,2),XY(:,3),[],XY(:,3),'s','filled');
colormap(jet)
colorbar;
