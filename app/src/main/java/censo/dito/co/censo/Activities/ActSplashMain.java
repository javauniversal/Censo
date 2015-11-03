package censo.dito.co.censo.Activities;


import android.content.Intent;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;

import censo.dito.co.censo.Entity.ConfigSplash;
import censo.dito.co.censo.R;
import censo.dito.co.censo.cnst.Flags;

public class ActSplashMain extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.color_blanco); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo_censo_2); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.DropOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("Flexible y adaptable.");
        configSplash.setTitleTextColor(R.color.textColorTitle);
        configSplash.setTitleTextSize(25f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }

    @Override
    public void animationsFinished() {
        //transit to another activity the activity here
        Bundle bundle = new Bundle();
        startActivity(new Intent(ActSplashMain.this, ActLogin.class).putExtras(bundle));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
