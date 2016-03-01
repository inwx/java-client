inwx.com XML-RPC Java Client
=========
You can access all functions of our frontend via an application programming interface (API). Our API is based on the XML-RPC protocol and thus can be easily addressed by almost all programming languages. The documentation and programming examples in PHP, Java, Ruby and Python can be downloaded here.

There is also an OT&E test system, which you can access via ote.inwx.com. Here you will find the known web interface which is using a test database. On the OTE system no actions will be charged. So you can test how to register domains etc.

Documentation
------
You can view a detailed description of the API functions in our documentation. The documentation as PDF ist part of the Projekt. You also can read the documentation online http://www.inwx.de/en/help/apidoc

Example
-------
The implementation in Java requires the included .jar files, which can be found in the lib/ directory.
For compiling you need to add these libraries to you classpath. In Eclipse this can be done with the following steps:
- Select all files in the lib/ directory
- Right click and go to 'Build path' -> 'Add to Build Path'

Now the files should disappear and listed in your referenced libraries. The .jar-files are still in your lib/ directory, they just not displayed there after adding to your build path.

If you don't use Eclipse and you want to compile it directly on your console, you can use the script 'compile_run.sh'.
The script should work without any changes if you doesn't change something like the class-name. If you customize some of this parts you have to manually edit the java/c parameters in the script.

License
----
MIT
