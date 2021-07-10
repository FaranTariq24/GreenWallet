package com.cutezilla.cryptomanager.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinDetail {
    private String id;
    private String symbol;
    private String name;
    private String asset_platform_id;
    HashMap<String, String> platforms;
    private float block_time_in_minutes;
    private String hashing_algorithm = null;
    ArrayList < Object > categories = new ArrayList< Object >();
    private String public_notice = null;
    ArrayList < Object > additional_notices = new ArrayList < Object > ();
    Localization localization;
    Description description;
    Links links;
    Image image;
    private String country_origin;
    private String genesis_date;
    private String contract_address;
    private float sentiment_votes_up_percentage;
    private float sentiment_votes_down_percentage;
    private float market_cap_rank;
    private float coingecko_rank;
    private float coingecko_score;
    private float developer_score;
    private float community_score;
    private float liquidity_score;
    private float public_interest_score;
    Public_interest_stats public_interest_stats;
    ArrayList < Object > status_updates = new ArrayList < Object > ();
    private String last_updated;

    public HashMap<String, String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(HashMap<String, String> platforms) {
        this.platforms = platforms;
    }

// Getter Methods

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getAsset_platform_id() {
        return asset_platform_id;
    }

//    public Platforms getPlatforms() {
//        return platforms;
//    }

    public float getBlock_time_in_minutes() {
        return block_time_in_minutes;
    }

    public String getHashing_algorithm() {
        return hashing_algorithm;
    }

    public String getPublic_notice() {
        return public_notice;
    }

    public Localization getLocalization() {
        return localization;
    }

    public Description getDescription() {
        return description;
    }

    public Links getLinks() {
        return links;
    }

    public Image getImage() {
        return image;
    }

    public String getCountry_origin() {
        return country_origin;
    }

    public String getGenesis_date() {
        return genesis_date;
    }

    public String getContract_address() {
        return contract_address;
    }

    public float getSentiment_votes_up_percentage() {
        return sentiment_votes_up_percentage;
    }

    public float getSentiment_votes_down_percentage() {
        return sentiment_votes_down_percentage;
    }

    public float getMarket_cap_rank() {
        return market_cap_rank;
    }

    public float getCoingecko_rank() {
        return coingecko_rank;
    }

    public float getCoingecko_score() {
        return coingecko_score;
    }

    public float getDeveloper_score() {
        return developer_score;
    }

    public float getCommunity_score() {
        return community_score;
    }

    public float getLiquidity_score() {
        return liquidity_score;
    }

    public float getPublic_interest_score() {
        return public_interest_score;
    }

    public Public_interest_stats getPublic_interest_stats() {
        return public_interest_stats;
    }

    public String getLast_updated() {
        return last_updated;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAsset_platform_id(String asset_platform_id) {
        this.asset_platform_id = asset_platform_id;
    }

//    public void setPlatforms(Platforms platformsObject) {
//        this.platforms = platformsObject;
//    }

    public void setBlock_time_in_minutes(float block_time_in_minutes) {
        this.block_time_in_minutes = block_time_in_minutes;
    }

    public void setHashing_algorithm(String hashing_algorithm) {
        this.hashing_algorithm = hashing_algorithm;
    }

    public void setPublic_notice(String public_notice) {
        this.public_notice = public_notice;
    }

    public void setLocalization(Localization localizationObject) {
        this.localization = localizationObject;
    }

    public void setDescription(Description descriptionObject) {
        this.description = descriptionObject;
    }

    public void setLinks(Links linksObject) {
        this.links = linksObject;
    }

    public void setImage(Image imageObject) {
        this.image = imageObject;
    }

    public void setCountry_origin(String country_origin) {
        this.country_origin = country_origin;
    }

    public void setGenesis_date(String genesis_date) {
        this.genesis_date = genesis_date;
    }

    public void setContract_address(String contract_address) {
        this.contract_address = contract_address;
    }

    public void setSentiment_votes_up_percentage(float sentiment_votes_up_percentage) {
        this.sentiment_votes_up_percentage = sentiment_votes_up_percentage;
    }

    public void setSentiment_votes_down_percentage(float sentiment_votes_down_percentage) {
        this.sentiment_votes_down_percentage = sentiment_votes_down_percentage;
    }

    public void setMarket_cap_rank(float market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public void setCoingecko_rank(float coingecko_rank) {
        this.coingecko_rank = coingecko_rank;
    }

    public void setCoingecko_score(float coingecko_score) {
        this.coingecko_score = coingecko_score;
    }

    public void setDeveloper_score(float developer_score) {
        this.developer_score = developer_score;
    }

    public void setCommunity_score(float community_score) {
        this.community_score = community_score;
    }

    public void setLiquidity_score(float liquidity_score) {
        this.liquidity_score = liquidity_score;
    }

    public void setPublic_interest_score(float public_interest_score) {
        this.public_interest_score = public_interest_score;
    }

    public void setPublic_interest_stats(Public_interest_stats public_interest_statsObject) {
        this.public_interest_stats = public_interest_statsObject;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}
class Public_interest_stats {
    private float alexa_rank;
    private String bing_matches = null;


    // Getter Methods

    public float getAlexa_rank() {
        return alexa_rank;
    }

    public String getBing_matches() {
        return bing_matches;
    }

    // Setter Methods

    public void setAlexa_rank(float alexa_rank) {
        this.alexa_rank = alexa_rank;
    }

    public void setBing_matches(String bing_matches) {
        this.bing_matches = bing_matches;
    }
}
class Image {
    private String thumb;
    private String small;
    private String large;


    // Getter Methods

    public String getThumb() {
        return thumb;
    }

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }

    // Setter Methods

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
class Links {
    ArrayList < String > homepage = new ArrayList < String > ();
    ArrayList < String > blockchain_site = new ArrayList < String > ();
    ArrayList < String > official_forum_url = new ArrayList < String > ();
    ArrayList < String > chat_url = new ArrayList < String > ();
    ArrayList < String > announcement_url = new ArrayList < String > ();
    private String twitter_screen_name;
    private String facebook_username;
    private String bitcointalk_thread_identifier = null;
    private String telegram_channel_identifier;
    private String subreddit_url;
    Repos_url Repos_urlObject;

    public ArrayList<String> getHomepage() {
        return homepage;
    }

    public ArrayList<String> getBlockchain_site() {
        return blockchain_site;
    }

    public ArrayList<String> getOfficial_forum_url() {
        return official_forum_url;
    }

    public ArrayList<String> getChat_url() {
        return chat_url;
    }

    public ArrayList<String> getAnnouncement_url() {
        return announcement_url;
    }

    public Repos_url getRepos_urlObject() {
        return Repos_urlObject;
    }

// Getter Methods

    public String getTwitter_screen_name() {
        return twitter_screen_name;
    }

    public String getFacebook_username() {
        return facebook_username;
    }

    public String getBitcointalk_thread_identifier() {
        return bitcointalk_thread_identifier;
    }

    public String getTelegram_channel_identifier() {
        return telegram_channel_identifier;
    }

    public String getSubreddit_url() {
        return subreddit_url;
    }

    public Repos_url getRepos_url() {
        return Repos_urlObject;
    }

    // Setter Methods

    public void setTwitter_screen_name(String twitter_screen_name) {
        this.twitter_screen_name = twitter_screen_name;
    }

    public void setFacebook_username(String facebook_username) {
        this.facebook_username = facebook_username;
    }

    public void setBitcointalk_thread_identifier(String bitcointalk_thread_identifier) {
        this.bitcointalk_thread_identifier = bitcointalk_thread_identifier;
    }

    public void setTelegram_channel_identifier(String telegram_channel_identifier) {
        this.telegram_channel_identifier = telegram_channel_identifier;
    }

    public void setSubreddit_url(String subreddit_url) {
        this.subreddit_url = subreddit_url;
    }

    public void setRepos_url(Repos_url repos_urlObject) {
        this.Repos_urlObject = repos_urlObject;
    }
}
class Repos_url {
    ArrayList < Object > github = new ArrayList < Object > ();
    ArrayList < Object > bitbucket = new ArrayList < Object > ();


    // Getter Methods



    // Setter Methods


}

class Localization {
    private String en;
    private String de;
    private String es;
    private String fr;
    private String it;
    private String pl;
    private String ro;
    private String hu;
    private String nl;
    private String pt;
    private String sv;
    private String vi;
    private String tr;
    private String ru;
    private String ja;
    private String zh;
    private String ko;
    private String ar;
    private String th;
    private String id;


    // Getter Methods

    public String getEn() {
        return en;
    }

    public String getDe() {
        return de;
    }

    public String getEs() {
        return es;
    }

    public String getFr() {
        return fr;
    }

    public String getIt() {
        return it;
    }

    public String getPl() {
        return pl;
    }

    public String getRo() {
        return ro;
    }

    public String getHu() {
        return hu;
    }

    public String getNl() {
        return nl;
    }

    public String getPt() {
        return pt;
    }

    public String getSv() {
        return sv;
    }

    public String getVi() {
        return vi;
    }

    public String getTr() {
        return tr;
    }

    public String getRu() {
        return ru;
    }

    public String getJa() {
        return ja;
    }

    public String getZh() {
        return zh;
    }


    public String getKo() {
        return ko;
    }

    public String getAr() {
        return ar;
    }

    public String getTh() {
        return th;
    }

    public String getId() {
        return id;
    }

    // Setter Methods

    public void setEn(String en) {
        this.en = en;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public void setRo(String ro) {
        this.ro = ro;
    }

    public void setHu(String hu) {
        this.hu = hu;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public void setKo(String ko) {
        this.ko = ko;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public void setId(String id) {
        this.id = id;
    }
}

