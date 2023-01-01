from datetime import datetime

AUTHOR = 'Teena George'
SITENAME = 'Articles on Java and Kotlin'
SITEURL = ''
SITETITLE = 'Articles on Java and Kotlin'
SITELOGO = SITEURL + 'images/profile.png'
FAVICON = 'images/favicon.ico'
BROWSER_COLOR = '#333333'
PYGMENTS_STYLE = "monokai"

ROBOTS = 'index, follow'
DISABLE_URL_HASH = True
MENUITEMS = (
    ("Archives", "/archives.html"),
    ("Categories", "/categories.html"),
    ("Tags", "/tags.html"),
)

CC_LICENSE = {
    "name": "Creative Commons Attribution-ShareAlike",
    "version": "4.0",
    "slug": "by-sa"
}

COPYRIGHT_YEAR = datetime.now().year

USE_FOLDER_AS_CATEGORY = False
MAIN_MENU = True
HOME_HIDE_TAGS = True

GITHUB_CORNER_URL = "https://github.com/teenageorge"

DATE_FORMATS = {
    "en": "%B %d, %Y",
}

DEFAULT_LANG = "en"

PATH = 'content'

TIMEZONE = 'Asia/Singapore'

FEED_ALL_ATOM = None
CATEGORY_FEED_ATOM = None
TRANSLATION_FEED_ATOM = None
AUTHOR_FEED_ATOM = None
AUTHOR_FEED_RSS = None

DEFAULT_PAGINATION = 10

THEME_COLOR_AUTO_DETECT_BROWSER_PREFERENCE = True
THEME_COLOR_ENABLE_USER_OVERRIDE = True

USE_LESS = True

THEME = 'theme/Flex'
STATIC_PATHS = ['images']