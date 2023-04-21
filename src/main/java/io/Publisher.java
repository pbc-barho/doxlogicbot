package io;

import solver.TableauSolver;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.Properties;

/**
 * A class that implements the Twitter publisher. This publisher posts a tweet every 3 hours, containing a tautology.
 */
public class Publisher implements Runnable {

    private Twitter twitter;
    private final TableauSolver solver;

    /**
     * Constructor for the Twitter publisher.
     * @param solver The tableau solver that is used to evaluate the formulas.
     */
    public Publisher(TableauSolver solver) {
        this.solver = solver;
    }

    /**
     * A method that initializes the Twitter bot.
     * @throws IOException An exception that is thrown if the Twitter bot cannot be initialized.
     */
    public void initTwitter() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/twitter4j.properties"));
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled("true".equals(properties.getProperty("debug")))
                .setOAuthConsumerKey(properties.getProperty("oauth.consumerKey"))
                .setOAuthConsumerSecret(properties.getProperty("oauth.consumerSecret"))
                .setOAuthAccessToken(properties.getProperty("oauth.accessToken"))
                .setOAuthAccessTokenSecret(properties.getProperty("oauth.accessTokenSecret"));
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    /**
     * The main method that runs the Twitter bot. It first initializes the bot using the initialization method and then
     * Chooses a random tautology to post on Twitter every three hours.
     */
    @Override
    public void run() {
        try {
            initTwitter();
            while (true){
                if (solver.getTautologies().size() == 0) {
                    Thread.sleep(3600000);
                } else {
                    try {
                        int randomIndex = (int)(Math.random() * solver.getTautologies().size());
                        Thread.sleep(300000);
                        String tweet = solver.getTautologies().get(randomIndex).getFormulaTree().getString();
                        solver.getTautologies().remove(randomIndex);
                        twitter.updateStatus(tweet);
                        Thread.sleep(10500000);
                    } catch(Exception e) {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not initialize the Twitter API.");
        }
    }
}
