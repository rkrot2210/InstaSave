package com.rk.realswipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    SearchView searchView;
    String endCursor = "";

    String url;
    String jsonStr="";
    List<Fragment> list = new ArrayList<>();
    List<String> arrayShortCode;

    public MainActivity() throws JSONException {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        arrayShortCode = new ArrayList<>();
        searchView = findViewById(R.id.searchBar);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                url = "https://www.instagram.com/" +query.trim()+"/?__a=1";

                new ParserProfile().execute();
                new ParserVideo().execute();
              new Parser().execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    class ParserProfile extends AsyncTask<Void, Void, Void> {

        String profileUrl ="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if( url != null) {
                    HttpHandler sh = new HttpHandler();
                    jsonStr = sh.makeServiceCall(url);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject graphqlFirst = jsonObj.getJSONObject("graphql");
                    JSONObject user = graphqlFirst.getJSONObject("user");
                    profileUrl = user.getString("profile_pic_url_hd");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);




         //   list.add(new PageFragmentVideo());
            if(url != null)
            list.add(new PageFragmentProfile(profileUrl));
            viewPager = findViewById(R.id.pager);
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);

            viewPager.setAdapter(pagerAdapter);

        }
    }

    class Parser extends AsyncTask<Void, Void, Void> {

        List<String>  photoList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if(url != null) {
                    HttpHandler sh = new HttpHandler();
                    jsonStr = sh.makeServiceCall(url);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject graphqlFirst = jsonObj.getJSONObject("graphql");
                    JSONObject user = graphqlFirst.getJSONObject("user");
                    JSONObject instaPost = user.getJSONObject("edge_owner_to_timeline_media");
                    JSONObject page_info = instaPost.getJSONObject("page_info");

                    endCursor = page_info.getString("end_cursor");


                    JSONArray posts = instaPost.getJSONArray("edges");
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject nodesObject = posts.getJSONObject(i);
                        JSONObject node = nodesObject.getJSONObject("node");
                        arrayShortCode.add(node.getString("shortcode"));
                        //System.out.println(arrayShortCode + "!!!!!!!!!!!!!!!!!!!!!");

                    }


                    for (int i = 0; i < arrayShortCode.size(); i++) {
                        String urlShortCode = "https://www.instagram.com/p/" + arrayShortCode.get(i) + "/?__a=1";
                        HttpHandler sh2 = new HttpHandler();
                        String jsonString = sh2.makeServiceCall(urlShortCode);
                        JSONObject jsonObj2 = new JSONObject(jsonString);
                        JSONObject graphql = jsonObj2.getJSONObject("graphql");
                        JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");

                        //  JSONObject edge_sidecar_to_children  = shortcode_media.getJSONObject("edge_sidecar_to_children");

                        if (!shortcode_media.isNull("edge_sidecar_to_children")) {
                            JSONObject edge_sidecar_to_children = shortcode_media.getJSONObject("edge_sidecar_to_children");
                            JSONArray edges = edge_sidecar_to_children.getJSONArray("edges");
                            for (int j = 0; j < edges.length(); j++) {
                                JSONObject nodesObjects = edges.getJSONObject(j);
                                JSONObject nodeImg = nodesObjects.getJSONObject("node");
                                if (!nodeImg.getBoolean("is_video")) {
                                    JSONArray display_resourceses = nodeImg.getJSONArray("display_resources");
                                    JSONObject srcImage = display_resourceses.getJSONObject(2);
                                    photoList.add(srcImage.getString("src"));
                                }
                            }
                        } else {
                            System.out.println("Object Is Nuuuuuuuuuuuuuuuuuuuuuuuuulllllllllll");
                            if (!shortcode_media.getBoolean("is_video")) {
                                JSONArray display_resources = shortcode_media.getJSONArray("display_resources");
                                JSONObject srcImage = display_resources.getJSONObject(2);
                                photoList.add(srcImage.getString("src"));
                            } else {
                                continue;
                            }
                        }

                        //JSONObject srcImage  = edge_sidecar_to_children.getJSONObject(2);

                    }
                }
                else{
                    for (int i = 0; i < arrayShortCode.size(); i++) {
                       // https://www.instagram.com/p/CCbqyO7FxwF/?igshid=s2hmitz9webk
                     //   https://www.instagram.com/p/" + arrayShortCode.get(i) + "/?__a=1
                        String urlShortCode = "https://www.instagram.com/p/" + arrayShortCode.get(i) + "/?__a=1";
                        HttpHandler sh2 = new HttpHandler();
                        String jsonString = sh2.makeServiceCall(urlShortCode);
                        JSONObject jsonObj2 = new JSONObject(jsonString);
                        JSONObject graphql = jsonObj2.getJSONObject("graphql");
                        JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");

                        //  JSONObject edge_sidecar_to_children  = shortcode_media.getJSONObject("edge_sidecar_to_children");

                        if (!shortcode_media.isNull("edge_sidecar_to_children")) {
                            JSONObject edge_sidecar_to_children = shortcode_media.getJSONObject("edge_sidecar_to_children");
                            JSONArray edges = edge_sidecar_to_children.getJSONArray("edges");
                            for (int j = 0; j < edges.length(); j++) {
                                JSONObject nodesObjects = edges.getJSONObject(j);
                                JSONObject nodeImg = nodesObjects.getJSONObject("node");
                                if (!nodeImg.getBoolean("is_video")) {
                                    JSONArray display_resourceses = nodeImg.getJSONArray("display_resources");
                                    JSONObject srcImage = display_resourceses.getJSONObject(2);
                                    photoList.add(srcImage.getString("src"));
                                }
                            }
                        } else {
                            System.out.println("Object Is Nuuuuuuuuuuuuuuuuuuuuuuuuulllllllllll");
                            if (!shortcode_media.getBoolean("is_video")) {
                                JSONArray display_resources = shortcode_media.getJSONArray("display_resources");
                                JSONObject srcImage = display_resources.getJSONObject(2);
                                photoList.add(srcImage.getString("src"));
                            } else {
                                continue;
                            }
                        }

                        //JSONObject srcImage  = edge_sidecar_to_children.getJSONObject(2);

                    }
                }

                    arrayShortCode.clear();

                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            list.add(new PageFragmentPhoto(photoList));

//photoList.clear();

            viewPager = findViewById(R.id.pager);
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
            viewPager.setAdapter(pagerAdapter);



        }
    }
    // this is parser video data from instagram
    class ParserVideo extends AsyncTask<Void, Void, Void> {

        ArrayList<MediaObject>  videoUrlList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if(url != null) {
                    HttpHandler sh = new HttpHandler();
                    jsonStr = sh.makeServiceCall(url);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject graphqlFirst = jsonObj.getJSONObject("graphql");
                    JSONObject user = graphqlFirst.getJSONObject("user");
                    JSONObject instaPost = user.getJSONObject("edge_owner_to_timeline_media");
                    JSONObject page_info = instaPost.getJSONObject("page_info");

                    endCursor = page_info.getString("end_cursor");


                    JSONArray posts = instaPost.getJSONArray("edges");
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject nodesObject = posts.getJSONObject(i);
                        JSONObject node = nodesObject.getJSONObject("node");
                        if (node.getBoolean("is_video")) {
                            arrayShortCode.add(node.getString("shortcode"));
                            System.out.println(arrayShortCode + "!!!!!!!!!!!!!!!!!!!!!");
                        } else {
                            continue;
                        }

                    }
                    for (int i = 0; i < arrayShortCode.size(); i++) {
                        String urlShortCode = "https://www.instagram.com/p/" + arrayShortCode.get(i) + "/?__a=1";
                        //   https://www.instagram.com/p/CCjO4H9Fd3I/?__a=1
                        HttpHandler sh2 = new HttpHandler();
                        String jsonString = sh2.makeServiceCall(urlShortCode);
                        JSONObject jsonObj2 = new JSONObject(jsonString);
                        JSONObject graphql = jsonObj2.getJSONObject("graphql");
                        JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");

                        //  JSONObject edge_sidecar_to_children  = shortcode_media.getJSONObject("edge_sidecar_to_children");

                        if (!shortcode_media.isNull("edge_sidecar_to_children")) {
                            JSONObject edge_sidecar_to_children = shortcode_media.getJSONObject("edge_sidecar_to_children");
                            JSONArray edges = edge_sidecar_to_children.getJSONArray("edges");
                            for (int j = 0; j < edges.length(); j++) {
                                JSONObject nodesObjects = edges.getJSONObject(j);
                                JSONObject nodeImg = nodesObjects.getJSONObject("node");
                                if (nodeImg.getBoolean("is_video")) {
                                    JSONArray display_resourceses = nodeImg.getJSONArray("display_resources");
                                    JSONObject srcImage = display_resourceses.getJSONObject(2);
                                    MediaObject videoItem = new MediaObject("Instagram ", nodeImg.getString("video_url"),
                                            srcImage.getString("src"), "resorse");
                                    videoUrlList.add(videoItem);
                                }
                            }
                        } else {
                            System.out.println("Object Is Nuuuuuuuuuuuuuuuuuuuuuuuuulllllllllll");
                            if (shortcode_media.getBoolean("is_video")) {
                                JSONArray display_resources = shortcode_media.getJSONArray("display_resources");
                                JSONObject srcImage = display_resources.getJSONObject(2);
                                MediaObject videoItem = new MediaObject("Instagram ", shortcode_media.getString("video_url"),
                                        srcImage.getString("src"), "resorse");
                                videoUrlList.add(videoItem);
                                // photoList.add(srcImage.getString("src"));
                            } else {
                                continue;
                            }
                        }

                        //JSONObject srcImage  = edge_sidecar_to_children.getJSONObject(2);

                    }
                }
                else{
                    for (int i = 0; i < arrayShortCode.size(); i++) {
                        String urlShortCode = "https://www.instagram.com/p/" + arrayShortCode.get(i) + "/?__a=1";
                        //   https://www.instagram.com/p/CCjO4H9Fd3I/?__a=1
                        HttpHandler sh2 = new HttpHandler();
                        String jsonString = sh2.makeServiceCall(urlShortCode);
                        JSONObject jsonObj2 = new JSONObject(jsonString);
                        JSONObject graphql = jsonObj2.getJSONObject("graphql");
                        JSONObject shortcode_media = graphql.getJSONObject("shortcode_media");

                        //  JSONObject edge_sidecar_to_children  = shortcode_media.getJSONObject("edge_sidecar_to_children");

                        if (!shortcode_media.isNull("edge_sidecar_to_children")) {
                            JSONObject edge_sidecar_to_children = shortcode_media.getJSONObject("edge_sidecar_to_children");
                            JSONArray edges = edge_sidecar_to_children.getJSONArray("edges");
                            for (int j = 0; j < edges.length(); j++) {
                                JSONObject nodesObjects = edges.getJSONObject(j);
                                JSONObject nodeImg = nodesObjects.getJSONObject("node");
                                if (nodeImg.getBoolean("is_video")) {
                                    JSONArray display_resourceses = nodeImg.getJSONArray("display_resources");
                                    JSONObject srcImage = display_resourceses.getJSONObject(2);
                                    MediaObject videoItem = new MediaObject("Instagram ", nodeImg.getString("video_url"),
                                            srcImage.getString("src"), "resorse");
                                    videoUrlList.add(videoItem);
                                }
                            }
                        } else {
                            System.out.println("Object Is Nuuuuuuuuuuuuuuuuuuuuuuuuulllllllllll");
                            if (shortcode_media.getBoolean("is_video")) {
                                JSONArray display_resources = shortcode_media.getJSONArray("display_resources");
                                JSONObject srcImage = display_resources.getJSONObject(2);
                                MediaObject videoItem = new MediaObject("Instagram ", shortcode_media.getString("video_url"),
                                        srcImage.getString("src"), "resorse");
                                videoUrlList.add(videoItem);
                                // photoList.add(srcImage.getString("src"));
                            } else {
                                continue;
                            }
                        }

                        //JSONObject srcImage  = edge_sidecar_to_children.getJSONObject(2);

                    }
                }






                arrayShortCode.clear();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            //list.add(new PageFragmentPhoto(photoList));
            list.add(new PageFragmentVideo(videoUrlList));


            viewPager = findViewById(R.id.pager);
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
            viewPager.setAdapter(pagerAdapter);



        }
    }
}
