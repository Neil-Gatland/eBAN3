// javascript for menu with submenus
// by anders brownworth - anders-javascript@evantide.com

shuttle = 'a';

if (document.images) {
  var sectionImages = new Array();

  sectionImages[0] = "top_community";
  sectionImages[1] = "top_news";
  sectionImages[2] = "top_wares";
  sectionImages[3] = "top_troubleshooting";
  sectionImages[4] = "top_magazine";
  sectionImages[5] = "top_fun";
  sectionImages[6] = "top_topmac";
  

  for (n in sectionImages) {
    eval("var " + sectionImages[n] + "_off = new Image()");
    eval(sectionImages[n] + "_off.src = 'images/navbar/" + sectionImages[n] + "_off.gif'");
    eval("var " + sectionImages[n] + "_on = new Image()");
    eval(sectionImages[n] + "_on.src = 'images/navbar/" + sectionImages[n] + "_on.gif'");
  }

  var subsectionImages = new Array();
  var subsectionCounts = new Array();

  subsectionImages[0] = "sub_community";
  subsectionCounts[0] = 8;
  subsectionImages[1] = "sub_news";
  subsectionCounts[1] = 6;
  subsectionImages[2] = "sub_wares";
  subsectionCounts[2] = 4;
  subsectionImages[3] = "sub_troubleshooting";
  subsectionCounts[3] = 6;
  subsectionImages[4] = "sub_magazine";
  subsectionCounts[4] = 8;
  subsectionImages[5] = "sub_fun";
  subsectionCounts[5] = 7;
  subsectionImages[6] = "sub_topmac";
  subsectionCounts[6] = 3;

  for (n in subsectionImages) {
    eval("var " + subsectionImages[n] + " = new Image()");
    eval(subsectionImages[n] + ".src = 'images/navbar/" + subsectionImages[n] + ".gif'");

    x = 2;

    while ( x <= subsectionCounts[n] ) {
      eval("var " + subsectionImages[n] + x + " = new Image()");
      eval(subsectionImages[n] + x + ".src = 'images/navbar/" + subsectionImages[n] + "_f" + x + ".gif'");
      x ++;
    }
  }

  var rollMap = new Object();

  rollMap.sub_community1 = 2;
  rollMap.sub_community2 = 2;
  rollMap.sub_community3 = 3;
  rollMap.sub_community4 = 4;
  rollMap.sub_community5 = 5;
  rollMap.sub_community6 = 6;
  rollMap.sub_community7 = 6;
  rollMap.sub_community8 = 7;
  rollMap.sub_community9 = 7;
  rollMap.sub_community10 = 8;
  rollMap.sub_community11 = 8;
  rollMap.sub_community12 = 8;
  rollMap.sub_community13 = 8;
  rollMap.sub_community14 = 0;

  rollMap.sub_news1 = 2;
  rollMap.sub_news2 = 2;
  rollMap.sub_news3 = 3;
  rollMap.sub_news4 = 3;
  rollMap.sub_news5 = 4;
  rollMap.sub_news6 = 4;
  rollMap.sub_news7 = 5;
  rollMap.sub_news8 = 5;
  rollMap.sub_news9 = 6;
  rollMap.sub_news10 = 6;
  rollMap.sub_news11 = 6;
  rollMap.sub_news12 = 0;
  rollMap.sub_news13 = 0;
  rollMap.sub_news14 = 0;
  
  rollMap.sub_wares1 = 2;
  rollMap.sub_wares2 = 2;
  rollMap.sub_wares3 = 3;
  rollMap.sub_wares4 = 3;
  rollMap.sub_wares5 = 3;
  rollMap.sub_wares6 = 4;
  rollMap.sub_wares7 = 4;
  rollMap.sub_wares8 = 4;
  rollMap.sub_wares9 = 0;
  rollMap.sub_wares10 = 0;
  rollMap.sub_wares11 = 0;
  rollMap.sub_wares12 = 0;
  rollMap.sub_wares13 = 0;
  rollMap.sub_wares14 = 0;

  rollMap.sub_troubleshooting1 = 2;
  rollMap.sub_troubleshooting2 = 2;
  rollMap.sub_troubleshooting3 = 3;
  rollMap.sub_troubleshooting4 = 3;
  rollMap.sub_troubleshooting5 = 4;
  rollMap.sub_troubleshooting6 = 4;
  rollMap.sub_troubleshooting7 = 5;
  rollMap.sub_troubleshooting8 = 5;
  rollMap.sub_troubleshooting9 = 6;
  rollMap.sub_troubleshooting10 = 6;
  rollMap.sub_troubleshooting11 = 0;
  rollMap.sub_troubleshooting12 = 0;
  rollMap.sub_troubleshooting13 = 0;
  rollMap.sub_troubleshooting14 = 0;

  rollMap.sub_magazine1 = 2;
  rollMap.sub_magazine2 = 2;
  rollMap.sub_magazine3 = 3;
  rollMap.sub_magazine4 = 4;
  rollMap.sub_magazine5 = 4;
  rollMap.sub_magazine6 = 5;
  rollMap.sub_magazine7 = 5;
  rollMap.sub_magazine8 = 6;
  rollMap.sub_magazine9 = 6;
  rollMap.sub_magazine10 = 7;
  rollMap.sub_magazine11 = 8;
  rollMap.sub_magazine12 = 8;
  rollMap.sub_magazine13 = 0;
  rollMap.sub_magazine14 = 0;

  rollMap.sub_fun1 = 2;
  rollMap.sub_fun2 = 3;
  rollMap.sub_fun3 = 3;
  rollMap.sub_fun4 = 3;
  rollMap.sub_fun5 = 4;
  rollMap.sub_fun6 = 5;
  rollMap.sub_fun7 = 6;
  rollMap.sub_fun8 = 6;
  rollMap.sub_fun9 = 7;
  rollMap.sub_fun10 = 7;
  rollMap.sub_fun11 = 7;
  rollMap.sub_fun12 = 0;
  rollMap.sub_fun13 = 0;
  rollMap.sub_fun14 = 0;

  rollMap.sub_topmac1 = 2;
  rollMap.sub_topmac2 = 2;
  rollMap.sub_topmac3 = 2;
  rollMap.sub_topmac4 = 3;
  rollMap.sub_topmac5 = 3;
  rollMap.sub_topmac6 = 3;
  rollMap.sub_topmac7 = 0;
  rollMap.sub_topmac8 = 0;
  rollMap.sub_topmac9 = 0;
  rollMap.sub_topmac10 = 0;
  rollMap.sub_topmac11 = 0;
  rollMap.sub_topmac12 = 0;
  rollMap.sub_topmac13 = 0;
  rollMap.sub_topmac14 = 0;

  var clickMap = new Object();

  clickMap.sub_community2 = "/community/reallifemac/";
  clickMap.sub_community3 = "/community/events/";
  clickMap.sub_community4 = "/community/forums/";
  clickMap.sub_community5 = "/community/mailbag/";
  clickMap.sub_community6 = "/community/artgallery/";
  clickMap.sub_community7 = "/community/usergroups/";
  clickMap.sub_community8 = "/community/addict_power/";

  clickMap.sub_news2 = "/content/news/buzzarchives/";
  clickMap.sub_news3 = "/news/newsletter/";
  clickMap.sub_news4 = "/content/news/productwatch/";
  clickMap.sub_news5 = "/news/reviews/";
  clickMap.sub_news6 = "/content/news/opinions/";
  
  clickMap.sub_wares2 = "/wares/osfiles/";
  clickMap.sub_wares3 = "/osx/";
  clickMap.sub_wares4 = "/wares/shareware/";

  clickMap.sub_troubleshooting2 = "/troubleshooting/doctor/";
  clickMap.sub_troubleshooting3 = "/troubleshooting/oldmac/";
  clickMap.sub_troubleshooting4 = "/troubleshooting/tips/";
  clickMap.sub_troubleshooting5 = "/troubleshooting/resources/";
  clickMap.sub_troubleshooting6 = "/troubleshooting/askus/";

  clickMap.sub_magazine2 = "/subscribe/";
  clickMap.sub_magazine3 = "/magazine/plugin";
  clickMap.sub_magazine4 = "/magazine/fix/";
  clickMap.sub_magazine5 = "/magazine/";
  clickMap.sub_magazine6 = "/magazine/backissue/";
  clickMap.sub_magazine7 = "/magazine/disc/";
  clickMap.sub_magazine8 = "/magazine/contact/";

  clickMap.sub_fun2 = "/content/fun/games/";
  clickMap.sub_fun3 = "/fun/seti/";
  clickMap.sub_fun4 = "/fun/movies/";
  clickMap.sub_fun5 = "/fun/music/";
  clickMap.sub_fun6 = "/maccam/";
  clickMap.sub_fun7 = "/fun/games/utserver.shtml";
  
  clickMap.sub_topmac2 = "/topmac/";
  clickMap.sub_topmac3 = "/topmac/join_us.shtml";
  
  if (navigator.appName.indexOf('Explorer') != -1) {
    var colorMap = new Object();

    colorMap.sub_community = "663366";
    colorMap.sub_news = "666633";
    colorMap.sub_wares = "663300";
    colorMap.sub_troubleshooting = "006666";
    colorMap.sub_magazine = "cc6600";
    colorMap.sub_fun = "666699";
    colorMap.sub_topmac = "666666";
  }
}

function topAct(imageName) {
  if (document.images) {
    document[imageName].src = eval(imageName + '_on.src');
    shuttle = imageName.replace(/top_/, "sub_");
    document.submenu.src = eval(shuttle + '.src');
    if (navigator.appName.indexOf('Explorer') != -1) {
      document.all.bar.style.backgroundColor = eval('colorMap.' + shuttle);
    }
  }
}

function topInact(imageName) {
  if (document.images) {
    document[imageName].src = eval(imageName + '_off.src');
  }
}

function subAct(sectionNumber) {
  if (document.images) {
    temp = eval("rollMap." + shuttle + sectionNumber);
    if ( temp > 0 ) {
      document['submenu'].src = eval(shuttle + temp + ".src");
    }
  }
}

function subInact(sectionNumber) {
  if (document.images) {
    temp = eval("rollMap." + shuttle + sectionNumber);
    if ( temp > 0 ) {
      document['submenu'].src = eval(shuttle + '.src');
    }
  }
}

function urlLoad(link) {
   var matchtest = navigator.appVersion.match(/MSIE\W+[0-4]\.[0-9]\W+Macintosh/);

   if ( matchtest == null ) {
    temp = eval("rollMap." + shuttle + link);
    if ( temp > 0 ) {
      document.location = eval("clickMap." + shuttle + temp);
    }
  }
}
