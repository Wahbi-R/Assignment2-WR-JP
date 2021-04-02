# Assignment2-WR-JP
Assignment 2 for 2020U\
Creators: Wahbi Raihan & Jacky Phung
This is a remote file sharing program, basically this program lets you choose a folder and download or upload files
from a remote server which is hosted by you as well.

Screenshot of program:
[An illustration of the expected output](https://github.com/Wahbi-Raihan/Assignment2-WR-JP/main/Working_Screenshot.png)

Improvements:
1. Coloured UI
2. Button effect on hover
3. Added a connect button that updates remote directory
4. Added the option to choose a local folder
5. Added a feature to delete files in local directory
6. Added a connection status to the bottom right
7. If on windows, can double click text files in local directory to open them.
8. Improved built in feature by now being able to move any file rather than just text files.

Other Resources:
1. Clone project to folder
1. Open IntelliJ
2. Go to File>Open>Cloned project 
3. Go to File>Project Structure>Project, ensure you're on Java 15 or newer (We used version 15.0.2)
4. Go to File>Project Structure>Libraries>plus button, Add in your javafx library sdk (We used 15.0.1)
5. Press Apply and Ok
6. Go to Run>Edit Configuration
7. Press the plus on the top left
8. Click on application
9. Click on Modify Options dropdown and hit "Add VM Options"
10. Add VM options like so: '--module-path "PATH TO JAVAFX LIBRARY" --add-modules javafx.controls,javafx.fxml'
11. Make sure the Main class is set to sample.Main
12. Hit Apply
13. Do step 7 to 11 again but this time Main class is sample.Server
14. Hit Apply and OK
15. On the top right hit the Run Arrow on Main
16. On the top right hit the Run Arrow on Server
17. Use the application!

Program Notes:
- To reconnect to the server everytime you have to hit Connect which will also renew the directory you see
- To upload a file select a file in the local files tab then tap upload (which moves from local to server)
- To download a file select a file in the remote files tab and tap the download button (which moves from server to local)
- Double click a file to open it
