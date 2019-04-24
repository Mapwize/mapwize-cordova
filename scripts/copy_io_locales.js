module.exports = function(ctx) {
    console.log("ios dir: " + ctx.opts.projectRoot);
    if (ctx.opts.platforms.indexOf('ios') < 0) {
        return;
    }
    const fs = require('fs-extra');
    const path = require('path');
    const xcode = require('xcode');

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

                console.log("getPbxPath: " + dirName);
                console.log("projectDir: " + projectDir);
            }
        })
        return projectDir;
    }

    console.log("source: " + platformSource + " dest: " + platformRes);
    var xcodeProjectPath = getXcodeProjPath(platformRes);

    console.log("xcodeProjectPath: " + xcodeProjectPath);

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
                console.log("baseDir: " + baseDir);

                files.forEach(function(file) {
                    var dirName = path.join(dir, file)
                    console.log("file: " + file);
                    var dirPath = fs.statSync(dirName);
                    
                    if (dirPath.isDirectory() && dirName.indexOf('lproj')  != -1) {
                        resFiles = fs.readdirSync(dirName);
                        resFiles.forEach(function(resFile) {
                            if (path.extname(resFile) == '.strings') {
                                var relFile = baseDir + "/" + file + "/" + resFile;
                                console.log("relFile: " + relFile);

                                var desc = xcodeProj.addResourceFile(relFile, {variantGroup:locVariantGroup, sourceTree: "SOURCE_ROOT"}, locVariantGroup);

                                console.log("resFile: " + relFile);
                                console.log("resFile desc: " + JSON.stringify(desc));
                            }
                        })
                    }
                    
                });
            };

    var pbxPath = getPbxPath(xcodeProjectPath);
    
    console.log('pbxproj: ' + pbxPath);
    xcodeProj = xcode.project(pbxPath);
    console.log('xcode init end...');

    // parsing is async, in a different process
    xcodeProj.parse(function (err) {
        console.log("Parse err: " + JSON.stringify(err));
        var locVariantGroup = xcodeProj.addLocalizationVariantGroup("Localizable.strings");
        console.log("locVariantGroup: " + JSON.stringify(locVariantGroup));
        addResources(xcodeProjectPath, [], xcodeProj, locVariantGroup.fileRef);
        var content = xcodeProj.writeSync();
       
        fs.writeFileSync(pbxPath, content);
        console.log('new project written');
    });





}