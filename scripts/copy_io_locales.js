module.exports = function(ctx) {
    console.log('Adding locales...');
    console.log("ios dir: " + ctx.opts.projectRoot);
    if (ctx.opts.platforms.indexOf('ios') < 0) {
        return;
    }
    const fs = require('fs-extra');
    const path = ctx.requireCordovaModule('path');
    const xcode = ctx.requireCordovaModule('xcode');
    var platformRes = path.join(ctx.opts.projectRoot, 'platforms/ios');
    var platformSource = path.join(ctx.opts.projectRoot, 'languages/ios');

    if (fs.pathExistsSync(platformRes) && fs.pathExistsSync(platformSource)) {
        console.log("source: " + platformSource + " dest: " + platformRes);
        fs.copySync(platformSource, platformRes);
    } else {
        console.log("no locales");
        return;
    }

    var getXcodeProjPath = function(root) {
        files = fs.readdirSync(root);
        var projectDir = null;
        files.forEach(function(file) {
            var dirName = path.join(root, file)
            var dirPath = fs.statSync(dirName);
            if (dirPath.isDirectory() && dirName.indexOf('xcodeproj')  != -1) {
                projectDir = dirName.replace('.xcodeproj', '');
            }
        })
        return projectDir;
    }

    var xcodeProjectPath = getXcodeProjPath(platformRes);
    fs.copySync(platformSource, xcodeProjectPath);

    var getPbxPath = function(projectRoot) {
        var pbxPath = path.join(projectRoot + '.xcodeproj', 'project.pbxproj');
        return pbxPath;
    }

    var addResources = function(dir, filelist, xcodeProj, locVariantGroup) {
                files = fs.readdirSync(dir);
                filelist = filelist || [];
                var dirs = dir.split('/');
                var baseDir = dirs[dirs.length - 1];

                files.forEach(function(file) {
                    var dirName = path.join(dir, file)
                    var dirPath = fs.statSync(dirName);
                    
                    if (dirPath.isDirectory() && dirName.indexOf('lproj')  != -1) {
                        resFiles = fs.readdirSync(dirName);
                        resFiles.forEach(function(resFile) {
                            if (path.extname(resFile) == '.strings') {
                                var relFile = baseDir + "/" + file + "/" + resFile;
                                var desc = xcodeProj.addResourceFile(relFile, {variantGroup:locVariantGroup, sourceTree: "SOURCE_ROOT"}, locVariantGroup);
                            }
                        })
                    }
                });
            };

    var pbxPath = getPbxPath(xcodeProjectPath);
    
    xcodeProj = xcode.project(pbxPath);

    // parsing is async, in a different process
    xcodeProj.parse(function (err) {
        var locVariantGroup = xcodeProj.addLocalizationVariantGroup("Localizable.strings");
        addResources(xcodeProjectPath, [], xcodeProj, locVariantGroup.fileRef);
        var content = xcodeProj.writeSync();
       
        fs.writeFileSync(pbxPath, content);
        console.log('new project written');
    });

}