Data-Federation
===============


Please refer to [here](https://github.com/pshken/data-federation/blob/master/About%20Data%20Federation%20Engine.pdf) for full doucmentation and more about this project.

### Quick Start

After cloning, go to src/templates/datasource.json and setup various data source properites. 

* datasource - It refers to the URL of the Restful services
* apiKey - API key for call the Restful services and if is not needed, just leave it empty.
* title - Refers to the data-source's name.
* key - It is like the "Primary Key" that connect various data sources.
* params - This is 1-to-1 mapping to the Restful parameters that users can call from Data Federation to the data-source.

After configured this file, go back the the main directory of the Data-Federation project and run "ant" to create the war file for deployment.

