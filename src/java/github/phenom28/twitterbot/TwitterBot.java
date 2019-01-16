package github.phenom28.twitterbot;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.util.Messages;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Ogundipe Segun David
 */
@Named
@SessionScoped
public class TwitterBot implements Serializable {

    private static final long serialVersionUID = -2012373683180403904L;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String stat;
    private String queryText;
    private String reply;
    private List<Status> stats = null;
    private List<Status> searchStats = null;
    private Twitter twitter;
    private boolean loggedIn = false;

    public TwitterBot() {
    }

    public void login() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory factory = new TwitterFactory(cb.build());
        twitter = factory.getInstance();
        loggedIn = factory.getInstance().getAuthorization().isEnabled();
    }
    
    public void sendTweet(){
       
        try {
            twitter.updateStatus(stat);
            addSuccessMessage("Tweet has been sent");
        } catch (TwitterException te) {
            Messages.addError(null, te.getErrorMessage());
        }
        
    }
    
    public void homeTimeline(){
        try {
            stats = twitter.getHomeTimeline();
        } catch (TwitterException te) {
            Messages.addError(null, te.getErrorMessage());
        }
    }
    
    public void searchForTweets(){
        Query query = new Query(queryText);
        QueryResult result;
        
        try {
            result = twitter.search(query);
            searchStats = result.getTweets();
        } catch (TwitterException te) {
            Messages.addError(null, te.getErrorMessage());
        }
    }
    
    public void replyToTweet(){
        Query query = new Query(queryText);
        QueryResult result;
        Status tweetResult;
        StatusUpdate statusUpdate;
        
        try {
            result = twitter.search(query);
            tweetResult = result.getTweets().get(0);
            statusUpdate = new StatusUpdate(".@" + tweetResult.getUser().getScreenName()
            +  " " + reply);
            statusUpdate.inReplyToStatusId(tweetResult.getId());
            twitter.updateStatus(statusUpdate);
        } catch (TwitterException te) {
            Messages.addError(null, te.getErrorMessage());
        }
    }
    
    public void addSuccessMessage(String msg) {
        FacesMessage facesMessage = Messages.create("").detail(msg).get();
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        Messages.add(null, facesMessage);
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<Status> getStats() {
        return stats;
    }

    public void setStats(List<Status> stats) {
        this.stats = stats;
    }

    public List<Status> getSearchStats() {
        return searchStats;
    }

    public void setSearchStats(List<Status> searchStats) {
        this.searchStats = searchStats;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
