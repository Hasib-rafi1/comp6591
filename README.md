## Annotated Relational Algebra System
#Project Description
In this project, you and your team are required to develop a running prototype for Annotated Relational Algebra System (or ARAS, for short), that implements the work of Green et al[PODS 2007] on provenance semirings, posted as paper P1 in the course web page. One input to ARAS will be an Annotated Relational Database D of some application, where the annotation "a" associated with each tuple "t" in each relation "R" in D could represent (1) the multiplicity of t (this means "R" is a bag), (2) the probability of t, (3) the certainty of t, relations for bag semantics), (4) provenance polynomial, or (5) 0 or 1 (for representing the standard case). The second input to the system will be any of the codes 1 to 5, where code i indicates the semantic of the annotations is as described in (i) above. The third and last input to the system is a positive relational algebra (RA) query over D in which the only operators allowed are projection π, select σ, union ∪, and natural join ./. Note that these are the only operators allowed in Datalog. We use the terms project, select, union, and natjoin to express RA expressions. For example, the RA query expression project < A, B > (q join r join s) means joing the relations q, r, and s and project on attributes A and B, and in so doing, consider the semantics of the annotations.

#To run the program 
  - Java
  - MySQL Installer 8.0.18 (https://dev.mysql.com/downloads/windows/installer/8.0.html)
  - mysql-connector-java-8.0.18.jar as external library
  - Change the config.properties acording to your database settings
  - all the dataset need to be inside the dbFiles. If the dataset contains anotation, please confirm the column name has to be "anotation"
  - If the dataset contains the anotation, Just press i for the importing the data. 
  - If the dataset does not conatin the anotation and you want us to genarate anotation based on the symentics. press a and continue to import the data acordingly.
  - to write the query we have a specific query structure . so you have to follow the parser syntex and order of the query. the sample query is given in the project as well as a sample query file is given in the dataset . You will find it inside the datasetzip file.
