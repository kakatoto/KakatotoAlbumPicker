
#Setup

#####Gradle

We will use cwac-camera for take a picture. And get library from jitpack.io

repositories {
    maven { url "https://jitpack.io" }

}

dependencies {
    compile 'com.github.kakatoto:KakatotoAlbumPicker:1.0.0'
}

#####Permission

Add permission for Camera, External Storage.

<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />

<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

#How to use
#####1. single image select

#####2. multiple image select
