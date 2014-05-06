function createfigure()
%CREATEFIGURE(X1,Y1,E1)
%  X1:  errorbar x
%  Y1:  errorbar y
%  E1:  errorbar e

Y1 = [144.2 92.2 62.2]
X1 = [2, 4, 8]
E1 = [1.92 2.04 3.63]

%  Auto-generated by MATLAB on 03-May-2014 20:26:57

% Create figure
figure1 = figure('PaperType','a4letter',...
    'PaperSize',[20.98404194812 29.67743169791]);

% Create axes
axes1 = axes('Parent',figure1,'YScale','log','YMinorTick','on',...
    'XTickLabel',{'2','4','8'},...
    'XTick',[2 4 8],...
    'XScale','log',...
    'XMinorTick','on',...
    'FontSize',14);
box(axes1,'on');
hold(axes1,'all');

% Create errorbar
errorbar(X1,Y1,E1,'LineWidth',1,...
    'Color',[0 0 1],...
    'Parent',axes1);

% Create xlabel
xlabel({'Number of Machines'},'FontSize',14);

% Create ylabel
ylabel({'Average Runtime (Seconds)'},'FontSize',14);

% Create legend
legend(axes1,'show');


