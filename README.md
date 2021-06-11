# Joke Machine
Don’t get stuck in Java 8 - Improved every day programming experience with Java 16 from Backend Developer perspective.

## The Purpose and what is inside

Te purpose of this project is to showcase as many features from Java 8 up to Java 16.PREVIEW 
as possible in form of plain old REST application which serves (mostly) funny jokes. 
All used features are described in the BlueSoft blog post which can be found [Here](https://link-url-here.org)


All the features for easy search are market with documentation comment like so: 

```Java
    /**
     * JAVA 14 FEATURE - SWITCH EXPRESSION
     */
```
Not all features from the article managed to be included in the application code base. Therefore some of them are presented in ```src/test/java/com/bluesoft/joke_machine/other_features_presentation``` package. 
## How the run the application

Although the presented features concerns the Java code only, the application is fully operational.

### Requirements
To run the application:
- Install Java 16.PREVIEW version
- Open it as Gradle project in IDE of choice

### Configuration
No additional configuration is required. 

### Usage
Application by default stars at localhost at port 8080.

There are three REST endpoints serving:
- **Jokes**
- **JokesCategories**
- **JokeProviders**

All served in JSON format.

##Endpoints

###Jokes 

**Purpose:**

Serves the joke based on the given parameters. Can be narrowed to given category, and/or provider.

**The base endpoint:**
- http://localhost:8080/jokes/random 

#####The request parameters:
- ```String``` provider-name (```optional```) - The name of one of the available providers
- ```String``` category (```optional```) - the category of the joke

###JokeCategories
**The purpose:**

Serves the set of all available joke categories from this application. Can be narrowed to one provider.

**The base endpoint:**
- http://localhost:8080/jokes/categories

#####The request parameters:
- ```String``` provider-name (```optional```) - The name of one of the available providers

###JokeProviders
**The purpose:**

Serves the set of underlying joke providers for this service. This information can be used for Jokes and Categories filtering.

**The base endpoint:**
- http://localhost:8080/jokes/providers





