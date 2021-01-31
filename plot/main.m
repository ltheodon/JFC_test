clear all;
close all;

% X1 = importdata("X1.txt");
% X2 = importdata("X2.txt");
% 
% figure;
% plot(X1(:,1),X1(:,2))
% hold on;
% plot(X2(:,1),X2(:,2))
% 
% 
% XY = importdata("XY.txt");
% 
% figure;
% %plot3(XY(:,1),XY(:,2),XY(:,3),'.','MarkerSize',15)
% scatter3(XY(:,1),XY(:,2),XY(:,3),[],XY(:,3),'s','filled');
% colormap(jet)
% colorbar;


% XY = importdata("RS2.txt");
% 
% figure;
% %plot3(XY(:,1),XY(:,2),XY(:,3),'.','MarkerSize',15)
% scatter3(XY(:,1),XY(:,2),XY(:,3),[],XY(:,3),'x');
% colormap(jet)
% colorbar;
% 
% 
% timePlot = importdata("timeComp.txt");
% 
% figure;
% plot(log(timePlot(:,1)),log(timePlot(:,2)),'o')
% hold on;
% plot(log(timePlot(:,1)),log(timePlot(:,3)),'o')
% legend('RS', 'Force brute');
% title('Temps de calcul (ms) en fonction de la taille du domaine (log-log)');
% 
% figure;
% plot(log(timePlot(:,1)),log(timePlot(:,3)),'x')
% title('Erreur relative en fonction de la taille du domaine (log-log)');



% A1 = importdata("A1.txt");
% A2 = importdata("A2.txt");
% figure;
% plot(A1(:,1),A1(:,2));
% hold on;
% C = abs(1-A2(:,1)/max(A2(:,1)));
% scatter(A2(:,2),A2(:,3),[],C,'fill')
% colormap(jet)
% % plot(A2(:,2),A2(:,3),'o');





% XY = importdata("A3.txt");
% 
% figure;
% scatter3(XY(:,2),XY(:,3),XY(:,4),[],XY(:,1),'s','filled');
% colormap(jet)
% colorbar;

% figure;
% scatter3(XY(:,2),XY(:,3),XY(:,4),[],XY(:,4),'x');
% colormap(jet)
% colorbar;


% A1 = importdata("A1.txt");
% figure;
% plot(A1(:,1),A1(:,2));
% A11 = importdata("A11.txt");
% hold on;
% C = abs(A11(:,1)/max(A11(:,1)));
% scatter(A11(:,2),A11(:,3),[],C,'fill')
% colormap(jet)


XY = importdata("B3c.txt");
% figure;
% scatter3(XY(:,2),XY(:,3),XY(:,4),[],XY(:,1),'s','filled');
% colormap(jet)
% colorbar;

figure;
for i = 1:10
    subplot(3,5,i);
    T1 = XY(XY(:,1)>1000*(10-i),1);
    T2 = XY(XY(:,1)>1000*(10-i),2);
    T3 = XY(XY(:,1)>1000*(10-i),3);
    T4 = XY(XY(:,1)>1000*(10-i),4);
    T22 = T2(T1<1000*(10-i+1)+1);
    T32 = T3(T1<1000*(10-i+1)+1);
    T42 = T4(T1<1000*(10-i+1)+1);
    if i < 10
        scatter3(T2,T3,T4,'k','s','filled'); hold on;
        scatter3(T22,T32,T42,'g','s','filled');
    else
        scatter3(T2,T3,T4,[],T1,'s','filled');
        colormap(jet)
    end
    view(2)
end

XY = importdata("Bf.txt");
figure;
scatter3(XY(:,1),XY(:,2),XY(:,3),[],XY(:,3),'s','filled');
colormap(jet)
colorbar;
view(2)
