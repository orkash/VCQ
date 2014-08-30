
% Z.k.

clc         % Clear screen
clear all   % Clear variables
close all   % Close trends
clearvars   % Clear variables from memory

javaaddpath('vcq.jar');     % add vcq.jar to matlab java class paths
javaclasspath -dynamic      % show java dynamic class paths 

currentWorkingDir = pwd;
vcm_main.vcq_main.main(currentWorkingDir);

i = 5;
while true
   fprintf('zkk %i\n',i);
   pause(3);
end

% Clear variables from memory 
clearvars;

% remove vcq.jar from matlab java class paths
javarmpath('vcq.jar');      
