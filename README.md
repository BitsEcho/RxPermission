# RxPermission

[ ![Download](https://api.bintray.com/packages/bitsecho/maven/rxpermission/images/download.svg?version=1.0.0) ](https://bintray.com/bitsecho/maven/rxpermission/1.0.0/link)

Android Kotlin ReactiveX permissions request tool with Androidx and RxJava3.

## Sample
(GIF maybe load slowly)

<img src="screenshot/sample.gif" width="30%" height="30%" />

## Usage
```kotlin
findViewById<Button>(R.id.camera).setOnClickListener {
    //Subscribe permission result by Observable. Manage Observable manually.
    var dis: Disposable? = null
    dis = requestRxPermission(Manifest.permission.CAMERA, Manifest.permission.VIBRATE).subscribe {
            val grant = if (it.isGrant) "Camera and Vibrate Grant" else "Camera or Vibrate Grant"
            Toast.makeText(this, grant, Toast.LENGTH_SHORT).show()
            dis?.dispose()
        }
}
```

```kotlin
findViewById<Button>(R.id.read_storage).setOnClickListener {
    //Trigger permission result once by code block. Briefer usage.
    requestPermissionOnce(Manifest.permission.READ_EXTERNAL_STORAGE) {
        val grant = if (it.isGrant) "Read Storage Grant" else "Read Storage NOT Grant"
        Toast.makeText(this, grant, Toast.LENGTH_SHORT).show()
    }
}
```

## Installation
```gradle
implementation 'com.bitsecho.rxpermission:rxpermission:1.0.0'
```

## License

**RxPermission** is under MIT license. See the [LICENSE](LICENSE) file for more info.
