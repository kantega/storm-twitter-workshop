## Requirements ##
* Apache Maven 3.x
* JVM 6 or 7 
* Internet connection for downloading maven dependencies and reading documentation
* IDE (Recommended: IntelliJ IDEA)

## General Info ##
* The folder cheating contains the entire solution, while the folder storm-assignment is the working directory for this workshop.
* Take a look at our [wiki](https://github.com/kantega/storm-twitter-workshop/wiki/Image-setup) for some guide, e.g. for the provided image file


## STEP ONE ##
### Download workshop projects
1. git clone https://github.com/kantega/storm-twitter-workshop

### For Eclipse-users only
1. Create Eclipse projects by running the `mvn eclipse:eclipse` command from the project root ("storm-twitter-workshop")
2. Run `mvn -Declipse.workspace="your Eclipse Workspace" eclipse:configure-workspace` and open this workspace from Eclipse
2. Import the storm-assignment project into Eclipse.

### Twitter Developer: Create keys and tokens
1. Go to [Twitter developer site](https://dev.twitter.com) and log in. Create new account if you do not already have one
2. Click on the arrow on top right beside your account image and select "My applications".
3. Create a new application. If you already have one, you can use credentials from that application and skip this and the next step.
4. Fill in the details. You can use dummy values for all required fields.
5. In the application detail page note the **Consumer key** and **Consumer secret** fields under OAuth settings
6. Create a new access token at the bottom of the page. The **Access token** and **Access token secret** will be shown after a moment.
7. Add these tokens to BOTH  
 ` storm-assignment / src / main / java / storm / starter / TwitterFunTopology.java` and  
 ` cheating / src / main / java / storm / starter / CheatingTwitterFunTopology.java`  
**OR** send them as command line arguments through maven by adding the following in step 8
``` -Dexec.args="CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET" ```
8. Test your credentials by running the main class in cheating

        cd cheating   
        mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=storm.starter.CheatingTwitterFunTopology
        
9. You should now start getting continuous output for 10 seconds. After this a file tweet.txt should have been generated that contains some twitter messages. If this file is empty, look at console output. 

Now, you are ready for coding. To the below steps on the storm-assignment folder and fill out the TODOs. 

## STEP TWO ##
### Storm: My first topology
Goal: Print out tweets  
Modify TwitterFunTopology.java: Define your Twitter query (location and topic based). See the provided TwitterSpout class and our [Reading tweet streams using Twitter4j](https://github.com/kantega/storm-twitter-workshop/wiki/Basic-Twitter-stream-reading-using-Twitter4j) wiki page for understanding how the tweets are fetched.  Build a topology that uses your twitter query and routes the twitter messages to the FileWriterBolt (Replace the todo's with implementations). Run the main method in TwitterFunTopology.java.
NOTE: Do not create more than 1 TwitterSpout workers or else you may encounter Twitter API limits! 


## STEP THREE ##
### Storm: Extracting hashtags
Goal: Build a topology that extracts and prints the hashtags from Norwegian tweets. We do this by introducing a new bolt (HashtagExtractionBolt). (Replace the todo's with implementations).  Print the hashtags to a FileWriterBolt.  
Hint: Take a look at [FilterQuery](https://github.com/kantega/storm-twitter-workshop/wiki/Twitter-API-and-Twitter4j-Streaming-Resources), however, note that the language feature may not yet implementet by Twitter4j. You can use the LanguageDetectionBolt to find the correct language, but it can give inaccurate results for a small language as Norwegain. You can use the location filter of FilterQuery get better accuracy. Again take a look at [Basic Twitter stream reading using Twitter4j](https://github.com/kantega/storm-twitter-workshop/wiki/Basic-Twitter-stream-reading-using-Twitter4j). Twitter4j documentation is poor, so it is recommended to use Twitters own documentation. In our [Twitter resource](https://github.com/kantega/storm-twitter-workshop/wiki/Twitter-API-and-Twitter4j-Streaming-Resources) we have listed relevant documentation from Twitter. Be sure to check that out! TODO: Explain location coordinate usage.


## STEP FOUR ##
### Storm: Count the popularity of hashtags
Goal: Build a topology that counts occurrences of the hashtags.
Hint: We do this by introducing a new bolt; the RollingCountBolt.
Print the results to a FileWriterBolt.
New concept:
    Sliding Windows - Time or size based frame for calculation objects

## STEP FIVE ##
### Storm: Rank the 10 most popular hashtags continuously
Goal: Show what people are discussing right now. Print a top 10 of the most popular hashtags to a FileWriterBolt.
Problem: How do we create such ranking if we do not store every event or hashtag?
Hint: We do this by introducing two new bolts; the IntermediateRankingsBolt and the TotalRankingsBolt.
We will route tuples from HashtagExtractionBolt --> RollingCountBolt --> IntermediateRankingsBolt --> TotalRankingsBolt --> FileWriterBolt
Example of routing:
```java
builder.setBolt("hashtags", new HashtagExtractionBolt(), 4).shuffleGrouping("YOUR SPOUT ID");
builder.setBolt("hashtag-counter", new RollingCountBolt(9, 3), 4).fieldsGrouping("hashtags", new Fields("entity"));
builder.setBolt("hashtag-intermediate-ranking", new IntermediateRankingsBolt(100), 4).fieldsGrouping("hashtag-counter", new Fields("obj"));
builder.setBolt("hashtag-total-ranking", new TotalRankingsBolt(100)).globalGrouping("hashtag-intermediate-ranking");
builder.setBolt("hashtag-ranking-print", new FileWriterBolt("HASHTAG_RANKING.txt")).shuffleGrouping("hashtag-total-ranking");
```

Information about how these bolts work: http://www.michael-noll.com/blog/2013/01/18/implementing-real-time-trending-topics-in-storm/
New concept:
    Tick tuples - When we require a bolt to “do something” at a fixed interval
    GlobalGrouping - We're using a TotalRankingsBolt-worker. Data from IntermediateRankingsBolt to TotalRankingsBolt is routed using globalGrouping



## STEP SIX ##
### Storm: Extracting sentiment values
Sentiment values are numerical measures of how positive or negative a given text is. By extracting sentiment values from tweets we can chart how positive people are to certain entities.
A common approach for extracting sentiment values is to apply dictionaries where different words are associated  with a sentiment value. Such dictionaries are customized for each language.
Goal: Extract the sentiment value for each of the tweets in your stream. Print the sentiment value to a FileWriterBolt.
To accomplish this task we need a topology with a bolt for language detection and a bolt for extracting the sentiment value.

## STEP SEVEN ##
### Storm: Calculate a running average value for sentiment
Goal: Calculate a running average sentiment value for the last 50 tweets. Print the average sentiment value to a FileWriterBolt.
To accomplish this will add the bolt to our topology: AverageWindowBolt.

## STEP EIGHT ##
### Storm: Its your playground!
Be creative - Form new twitter queries, create new bolts, combine them and extract knowledge!
For example:
* Find out where people are most happy, Oslo, Trondheim, Stavanger or Bergen?
* How happy are people about the weather, the public transport, etc.?
* Who is most popular: Jens Stoltenberg or Erna Solberg? What are their associated sentiment values?
