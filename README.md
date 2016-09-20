# StickySnot
If you want to make your views sticky and can be dragged like snot eh...And this library will show you a some way.

this can make you view behave like this:


![image](https://github.com/gamedirty/StickySnot/blob/master/gif/gif.gif?raw=true)

##how to use
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
	
		allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
**Step 2.**Add the dependency:

	dependencies {
	        compile 'com.github.gamedirty:StickySnot:2.1'
	}

then all you need is write few code to make your view lively。

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SnotPanel root = SnotPanel.attachToWindow(this);
        root.makeViewSoft(this, R.id.snot);
        root.makeViewSoft(this,R.id.lalalal);
        root.makeViewSoft(this,R.id.rect);
    }
    
