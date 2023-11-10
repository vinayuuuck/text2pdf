# text2pdf

## Running
The following is a maven project and so maven needs to be installed on the local machine.
To run the project you can build it using maven and run accordingly. I primarily used vscode during my development process and so it should ideally be able to run using the same.
The project does need to be provided an argument, which is the text file to be inputted containing the required test.
To do so, we can add a configuration in the launch.json file under the .vscode directory. This can be the absolute path of the file or relative to the current directory(“../input.txt” for example). The same can be done using IntelliJ as well.
Additionally, another command line argument can also be provided if we want to specify the name and location of the outputted pdf file, however, if this argument is not provided, the program will run just fine and simply use the same name and location as the input file.

## Working
The working of the program -

The file is read and each line is stored as a separate element in an array of strings. The efficiency of the program might be improved if the input file was open for the duration of the program as that would require less overhead, however, I opted to make sure that the file was open for as little time as possible, as other applications might need to access the file as well.
The arraylist is then iterated over and if the line does not start with a period, it is assumed to be a line of text and so the text is added to a text buffer and we continue.
If the line starts with a period, it is assumed that the line is a command, and so our existing hashmap of commands(which has been pre-fed with the default commands) is checked to see if the command exists. If it does, the command is set to true/given a value in the case of an indent, otherwise an error is thrown.
We can think of the commands as flags that are set to True if we want to manipulate the text using them or false if not. And so our text buffer is accordingly manipulated and added to a paragraph buffer. The text buffer is then cleared and the process continues.
If we come across a paragraph break or if the fill command is updated, we add the paragraph buffer to the document and clear the paragraph buffer.
Once the entire file has been read, we add the remaining text to the paragraph buffer which we add to the final document, which in turn is finally closed.

The program is written in such a way that it can be extended to add more commands. All that needs to be done is to add the command to the hashmap and add the corresponding functionality to the program.