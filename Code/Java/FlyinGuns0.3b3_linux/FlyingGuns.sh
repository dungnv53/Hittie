#!/bin/sh
export LD_LIBRARY_PATH=bin:$LD_LIBRARY_PATH
java -cp lib/swing-layout-1.0.1.jar:lib/genesisfx.jar:lib/StarfireExt.jar:lib/flyingguns_core_client.jar:lib/flyinggunsdata.jar:lib/hardcode_util.jar:lib/headquarter_core.jar:lib/j3dcore.jar:lib/j3dutils.jar:lib/jxinput.jar:lib/threed_java3d_core.jar:lib/vecmath.jar com.flyingguns.app.foo.Client