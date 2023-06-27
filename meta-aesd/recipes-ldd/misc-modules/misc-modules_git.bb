# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

inherit module

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-straend.git;protocol=ssh;branch=main \
           file://0001-Only-build-misc-modules-and-scull.patch \
           file://0001-PDE_DATA-replaced-by-pde_data.patch \
           file://init_modules \
           file://module_load \
           file://module_unload \
           " 

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "849ef219f38edc4fe5a9a39f7837ea0e2d2e0d98"
S = "${WORKDIR}/git"

INITSCRIPT_NAME = "init_modules"
INITSCRIPT_PARAMS = "start 99 S ."
inherit update-rc.d

# Use this if we don't have any custom loaders
#KERNEL_MODULE_AUTOLOAD += "hello"

# Only build the scull module
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
#MODULES_INSTALL_TARGET = "subdirs"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

# actually there are more modules but we don't want them
RPROVIDES:${PN} += "kernel-module-hello kernel-module-faulty"
FILES:${PN} += "${INIT_D_DIR}/${INITSCRIPT_NAME} ${bindir}/module_load ${bindir}/module_unload"

do_install:append () {
    # Add startupscript
    # Remember to inherit update-rc.d
    # and include in FILES
    install -d ${D}/${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}/${INIT_D_DIR}/${INITSCRIPT_NAME}

    install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/module_load ${D}${bindir}/
	install -m 0755 ${WORKDIR}/module_unload ${D}${bindir}/
}