module.exports = function(ctx) {
    console.log("android dir: " + ctx.opts.projectRoot);
    if (ctx.opts.platforms.indexOf('android') < 0) {
        return;
    }
    // var path = ctx.requireCordovaModule('path'),
    //     properties = ctx.requireCordovaModule("properties-parser");
    var path = require('path'),
        properties = require("properties-parser");

    var platformRoot = path.join(ctx.opts.projectRoot, 'platforms/android');
    var gradleProperties = path.join(platformRoot, 'gradle.properties');

    var editor = properties.createEditor(gradleProperties);
    editor.set("cdvMinSdkVersion", "21");
    editor.set("android.useAndroidX", "true");
    editor.set("android.enableJetifier", "true");
    editor.save();
}