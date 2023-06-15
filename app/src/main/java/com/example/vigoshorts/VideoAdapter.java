package com.example.vigoshorts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.io.FileNotFoundException;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoItems> myVideosList;
    Activity activity;
    private DocumentFile documentFile;
    private ViewPager2 viewPager2;

    public VideoAdapter(List<VideoItems> myVideosList, Activity activity, DocumentFile documentFile, ViewPager2 viewPager2) {
        this.myVideosList = myVideosList;
        this.activity = activity;
        this.documentFile = documentFile;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.title.setText(myVideosList.get(position).title);
        holder.description.setText(myVideosList.get(position).description);
        holder.videoView.setVideoURI(Uri.parse(myVideosList.get(position).video));
        holder.userid.setText(myVideosList.get(position).id);

        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.progressBar.setVisibility(View.GONE);
                mp.start();

                float  vidRatio = mp.getVideoWidth()/(float)mp.getVideoHeight();
                float screenRatio = holder.videoView.getWidth()/(float)holder.videoView.getHeight();

                float scale = vidRatio/screenRatio;

                if (scale>=1){

                    holder.videoView.setScaleX(scale);
                }else {

                    holder.videoView.setScaleY(1f/scale);
                }

            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int nextVideoIndex=position+1;
                if(nextVideoIndex>=myVideosList.size()){
                    Toast.makeText(activity, "This is last Video", Toast.LENGTH_SHORT).show();
                    viewPager2.setCurrentItem(0);
                }
                else {
                    viewPager2.setCurrentItem(nextVideoIndex);
                }

            }
        });

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Downloading...", Toast.LENGTH_SHORT).show();

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
                {
                    downloadAndroid11(position,documentFile);
                }
                else
                {
                    downloadForLessThan11(position);
                }
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT,"Watch awesome short Videos here.."+myVideosList.get(position).video);
//                activity.startActivity(intent);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
                {
                    downloadAndroid11ForSharing(position,documentFile);
                }
                else
                {
                    downloadForLessThan11ForSharing(position);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return myVideosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title,description,userid;
        ImageView like,shareBtn,downloadBtn,add,addNewVideoBTn, disk;
        CircleImageView profile;
        ProgressBar prog,progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView=itemView.findViewById(R.id.videoView);
            title=itemView.findViewById(R.id.textView);
            description= itemView.findViewById(R.id.textView2);
            userid=itemView.findViewById(R.id.textView3);
            like=itemView.findViewById(R.id.imageView5);
            downloadBtn=itemView.findViewById(R.id.imageView4);
            add=itemView.findViewById(R.id.imageView6);
            profile=itemView.findViewById(R.id.circleImageView);
            prog=itemView.findViewById(R.id.vidProg);
            shareBtn=itemView.findViewById(R.id.imageView3);
            addNewVideoBTn=itemView.findViewById(R.id.imageView2);
            progressBar=itemView.findViewById(R.id.progressBar);
            disk=(ImageView) itemView.findViewById(R.id.imageView);

        }

    }


//    for video downloading...
    public void downloadAndroid11(int position,DocumentFile fileDoc) {

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(myVideosList.get(position).video);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading : "+myVideosList.get(position).title+".mp4");


        request.setDestinationInExternalFilesDir(activity,fileDoc.getUri().getPath(),myVideosList.get(position).title+".mp4");

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        long reference = downloadManager.enqueue(request);



        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (reference == id) {
                    Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show();

                    try {
                        downloadManager.openDownloadedFile(id);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        activity.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void downloadForLessThan11(int position) {

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(myVideosList.get(position).video);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading : "+myVideosList.get(position).title+".mp4");


        request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS,myVideosList.get(position).title+".mp4");

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        long reference = downloadManager.enqueue(request);



        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (reference == id) {
                    Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show();

                    try {
                        downloadManager.openDownloadedFile(id);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        activity.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


//    for video sharing..
    public void downloadForLessThan11ForSharing(int position) {


        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Downloading Before Sharing..");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(myVideosList.get(position).video);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading : "+myVideosList.get(position).title+".mp4");


        request.setDestinationInExternalFilesDir(activity,Environment.DIRECTORY_DOWNLOADS,myVideosList.get(position).title+".mp4");

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        long reference = downloadManager.enqueue(request);



        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (reference == id) {
                    Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    Uri vidUri =    downloadManager.getUriForDownloadedFile(id);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("video/mp4");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,vidUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Check Out This Short Video - ");
                    activity.startActivity(shareIntent);

                }
            }
        };
        activity.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void downloadAndroid11ForSharing(int position,DocumentFile fileDoc) {


        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Downloading Before Sharing..");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(myVideosList.get(position).video);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading : "+myVideosList.get(position).title+".mp4");


        request.setDestinationInExternalFilesDir(activity,fileDoc.getUri().getPath(),myVideosList.get(position).title+".mp4");

        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        long reference = downloadManager.enqueue(request);



        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id

                if (reference == id) {


                    Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    Uri vidUri =  downloadManager.getUriForDownloadedFile(id);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("video/mp4");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,vidUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Check This Video - ");
                    activity.startActivity(shareIntent);




                }

            }
        };



        activity.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
