# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# DEV build
#SOURCE_PATH="/home/tomas/cor/ass3"

#SRC_URI = "file://${SOURCE_PATH}/"
#PV = "dev"
#S = "${WORKDIR}${SOURCE_PATH}/server"

# PROD build
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-straend;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
# https://github.com/cu-ecen-aeld/assignments-3-and-later-straend/commits/assignment-8-complete
SRCREV = "367130d3bd889dc2d100272bdfe40257b03b2e0a"
S = "${WORKDIR}/git/server"

## End Production

USERBIN = "/usr/bin"
# Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${USERBIN}/aesdsocket ${USERBIN}/aesdsocket-start-stop"

# customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-lpthread -lrt"

INITSCRIPT_NAME = "aesdsocket-start-stop"
INITSCRIPT_PARAMS = "start 98 S ."
RDEPENDS:${PN} += "libgcc"

inherit update-rc.d
#inherit 

#do_configure () {
#	:
#}

do_compile () {
	oe_runmake
	#make aesdsocket
}

do_install () {
	# Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	install -d ${D}${USERBIN}
	install -m 0755 ${S}/aesdsocket ${D}${USERBIN}/
	
	# Add startupscript
	# Remember to inherit update-rc.d
	install -d ${D}/${INIT_D_DIR}
    install -m 0755 ${S}/${INITSCRIPT_NAME} ${D}/${INIT_D_DIR}/${INITSCRIPT_NAME}
}
