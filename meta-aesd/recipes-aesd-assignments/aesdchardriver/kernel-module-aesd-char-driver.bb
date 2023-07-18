# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit module

#SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-straend;protocol=ssh;branch=main 
SRC_URI = " git:///home/ubuntu/assignments-3-and-later-straend/;protocol=file;branch=main \
           file://aesddriver-start-stop \
		  "


PV = "1.0+git${SRCPV}"
# https://github.com/cu-ecen-aeld/assignments-3-and-later-straend/commits/assignment-8-complete
#SRCREV = "1a9ecf2b38705b71b0d8c9d1f0b850605000ea8b"
SRCREV = "01c0ef962f3abaa56a06697c1b3019672f606cbd"

S = "${WORKDIR}/git/aesd-char-driver"

FILES:${PN} += "${INIT_D_DIR}/${INITSCRIPT_NAME}"

TARGET_LDFLAGS += "-lpthread -lrt"

INITSCRIPT_NAME = "aesddriver-start-stop"
INITSCRIPT_PARAMS = "start 95 S ."
RDEPENDS:${PN} += "libgcc"

inherit update-rc.d
# We're using a custom loader for loading
KERNEL_MODULE_AUTOLOAD += "aesdchar"

# Only build the scull module
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
#MODULES_INSTALL_TARGET = "aesd-char-driver"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"
RPROVIDES:${PN} += "kernel-module-aesd-char-driver"
FILES:${PN} += "${INIT_D_DIR}/${INITSCRIPT_NAME}"

do_install:append () {
#	# Add startupscript
#	# Remember to inherit update-rc.d#
	install -d ${D}/${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}/${INIT_D_DIR}/${INITSCRIPT_NAME}
}
