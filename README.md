## Requirements ##
* Apache Maven 3.x
* JVM 6 or 7 
* Internet connection for downloading maven dependencies and reading documentation
* Recommended: IntelliJ IDEA 

## STEP ONE ##
### Twitter Developer: Create keys and tokens
1. Go to [Twitter developer site](https://dev.twitter.com) and log in. Create new account if you do not already have one
2. Click on the arrow on top right beside your account image and select "My applications".
3. Create a new application. If you already have one, you can use credentials from that application and skip this and the next step.
4. Fill in the details. You can use dummy values for all required fields.
5. In the application detail page note the **Consumer key** and **Consumer secret** fields under OAuth settings
6. Create a new access token at the bottom of the page. The **Access token** and **Access token secret** will be shown after a moment.
7. Add these tokens to  
` storm-assignment / src / main / java / storm / starter / TwitterFunTopology.java` and  
` cheating / src / main / java / storm / starter / TwitterFunTopology.java` 
**OR** send them as command line arguments through maven by adding the following in step 8
```-Dexec.args="CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET" ```
8. Test your credentials by running the main class in cheating
```
cd cheating  
mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=storm.starter.TwitterFunTopology
```
9. You should now get continious output and no errors

## STEP TWO ##
### Storm: My first topology
Goal: Print out tweets
1. Define your Twitter query (location and topic based)
2. Build a topology that use your twitter query and routes the twitter messages to the PrinterBolt (Replace the todo's with implementations).

## STEP THREE ##
### Storm: Extracting hashtags
Goal: Build a topology that extracts and prints the hashtags from Norwegian tweets. We do this by introducing a new bolt.  
Hint: Take a look at [FilterQuery](https://github.com/kantega/storm-twitter-workshop/wiki/Twitter-API-and-Twitter4j-Streaming-Resources), however, note that the language feature is not yet implementet by Twitter4j.  
Still stuck? Take a look at [Basic Twitter stream reading using Twitter4j](https://github.com/kantega/storm-twitter-workshop/wiki/Basic-Twitter-stream-reading-using-Twitter4j). 

## STEP FOUR ##
### Storm: Count the popularity of hashtags
Goal: Build a topology that counts occurrences of the hashtags.
Hint: We do this by introducing a new bolt; the RollingCountBolt.

## STEP FIVE ##
### Storm: Rank the 10 most popular hashtags continuously
Goal: Show what people are discussing right now.
Problem: How do we create such ranking if we do not store every event or hashtag?
Hint: We do this by introducing two new bolts; the IntermediateRankingsBolt and the TotalRankingsBolt.

## STEP SIX ##
### Storm: Extracting sentiment values
Sentiment values are numerical measures of how positive or negative a given text is. By extracting sentiment values from tweets we can chart how positive people are to certain entities.
A common approach for extracting sentiment values is to apply dictionaries where different words are assosiated  with a sentiment value. Such dictionaries are customized for each language.
Goal: Extract the sentiment value for each of the tweets in your stream
To accomplish this task we need a topology with a bolt for language detection and a bolt for extracting the sentiment value.

## STEP SEVEN ##
### Storm: Calculate a running average value for sentiment
Goal: Calculate a running average sentiment value for the last 50 tweets
To accomplish this will add the bolt to our topology: AverageWindowBolt.

## STEP EIGHT ##
### Storm: Its your playground!
Be creative - Form new twitter queries, create new bolts, combine them and extract knowledge!
For example:
* Find out where people are most happy, Oslo, Trondheim, Stavanger or Bergen?
* How happy are people about the weather, the public transport, etc.?
* Who is most popular: Jens Stoltenberg or Erna Solberg? What are their associated sentiment values?
