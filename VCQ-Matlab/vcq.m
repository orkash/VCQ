

% Z.k.

clc         % Clear screen
clear all   % Clear variables
close all   % Close trends
clearvars   % Clear variables from memory

javaaddpath('vcq.jar');     % add vcq.jar to matlab java class paths
javaclasspath -dynamic      % show java dynamic class paths 

currentWorkingDir = pwd;

obj = vcm_main.vcq_main;
obj.main(currentWorkingDir);

bToContinue = true;
while bToContinue
   
   tic;
   command = obj.GetLastCommand();
   
   if command == 1
        fprintf('command(%i) fly up\n',command);
        toc;
   end
   if command == 2
        fprintf('command(%i) fly down\n',command);
        toc;
   end
   if command == 0
        fprintf('command(%i) turn off\n',command);
        bToContinue = false;
   end
end

% Clear variables from memory 
clearvars;

% remove vcq.jar from matlab java class paths
javarmpath('vcq.jar');      
