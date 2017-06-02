
# Setup

##### Gradle
We will use cwac-camera for take a picture. And use databinding And get library from jitpack.io


    repositories {
        maven { url "https://jitpack.io" }
    }

    dataBinding {
        enabled = true
    }
    
    dependencies {
        compile 'com.github.kakatoto:KakatotoAlbumPicker:1.0.0'
    }

##### Permission
Add permission for Camera, External Storage.

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

# How to use
##### 1. single image select
     AlbumPickerBuilder albumPickerBuilder = new AlbumPickerBuilder.Builder(context).setTitle("").
                                setOnImageSelectedListener(new AlbumPickerBuilder.OnImageSingleSelectedListener() {
                                    @Override
                                    public void onSingleSelected(ArrayList<Uri> imageList) {
                                        singleContent = imageList.get(0);
                                        view.setImeageContent(singleContent);
                                    }

                                }).build();
                        albumPickerBuilder.create();
                        
##### 2. multiple image select
    AlbumPickerBuilder albumPickerBuilder = new AlbumPickerBuilder.Builder(context).setTitle("").isMulti(true).
                                setOnImageSelectedListener(new AlbumPickerBuilder.OnImageSingleSelectedListener() {
                                    @Override
                                    public void onSingleSelected(ArrayList<Uri> imageList) {
                                        multiContents.clear();
                                        for (Uri uri : imageList)
                                            multiContents.add(uri);
                                        adapter.notifyDataSetChanged();
                                    }

                                }).build();
                        albumPickerBuilder.create();
                        
# Customize
##### sample
      AlbumPickerBuilder albumPickerBuilder = new AlbumPickerBuilder.Builder(context).setTitle("").isMulti(true).
                                setOnImageSelectedListener(onImageSelectedListener).build();
                                
##### function
* setTitle("title") (default : "")
* isMulti(true) (default : false) 
