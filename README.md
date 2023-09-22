# Template repository for Mykey micro-services 

## project structure:

* **main**:
  * **com.mykeyapi.template**:
    * **config**:
      * **AppProperties** - _const properties passed from application.yaml_
      * **KafkaAdmin** - _builds projects output topics_

    * **singles** - _objects you want to reuse in project_
    * **streams**: 
      * **StreamTemplate** - _a template class fro streaming events_

    * **ServiceTemplate** - _runs the project_

* **test**:
  * **com.mykeyapi.template**:
    * **testSingle**s - _stuff you want to reuse in tests_
    * **TestStreamsTemplate** - _template for testing streams between two topics_

 
 
In future we can add controllers template and other stuff that will repeat in our services 
    
    
   


      
