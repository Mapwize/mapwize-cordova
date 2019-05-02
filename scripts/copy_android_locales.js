module.exports = function(ctx) {
    console.log("android dir: " + ctx.opts.projectRoot);
    if (ctx.opts.platforms.indexOf('android') < 0) {
        return;
    }
    const fs = require('fs-extra');
    var path = require('path');

    var platformRes = path.join(ctx.opts.projectRoot, 'platforms/android/app/src/main/res');
    var platformSource = path.join(ctx.opts.projectRoot, 'languages/android');

    if (fs.pathExistsSync(platformRes) && fs.pathExistsSync(platformSource)) {
        console.log("source: " + platformSource + " dest: " + platformRes);
        fs.copySync(platformSource, platformRes);
    } else {
        console.log("no locales");
    }
    

}