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

### Advance Configuration

After you have download the default version of Data Federation, under the folder src/templates, there are 3 other configuration file for map.vm, reduce.vm and finalizer.vm. Basically these three files are the setting for MapReduce in MongoDB, which is writtern in Javascript. Ontop of these, Apache Velocity templating tool is being used. 

Currently, the keywords that are supported are $datasourceList are supported in all 3 files and $dataKey is supported in map.vm only. 

* $datasourceList refers to the list of datasources's title loaded from datasource.json, so therefore it is important to ensure that the title is correctly used.
* $dataKey refers to the "key" properties in datasource.json

Although you can write according to this template but for users who are able to produce their own map, reduce and finalizer functions are also able to copy and paste directly into these 3 files and as long do not use velocity formatting, the teamplting tool will have no effect on it.
