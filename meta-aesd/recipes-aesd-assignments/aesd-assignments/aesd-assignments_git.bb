# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-straend;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# Set to reference a specific commit hash in your assignment repo
SRCREV = "5d3bd1bc4da0f52e6caf27cd1f8cefd2e5c5c7aa"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket ${bindir}/aesdsocket-start-stop"

# customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-lpthread -lrt"

INITSCRIPT_NAME = "aesdsocket-start-stop"
INITSCRIPT_PARAMS = "start 98 S ."
RDEPENDS:${PN} += "libgcc"

inherit update-rc.d

do_configure () {
	:
}

do_compile () {
	# uncomment for debugging output
	#sed -i "s/#DEBUG 0/#DEBUG 1/g" ${S}/aesdsocket.c
	
	oe_runmake
}

do_install () {
	# Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	install -d ${D}${bindir}
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/
	
	# Add startupscript
	# Remember to inherit update-rc.d
	install -d ${D}/${INIT_D_DIR}
    install -m 0755 ${S}/${INITSCRIPT_NAME} ${D}/${INIT_D_DIR}/${INITSCRIPT_NAME}
}
