echo "CHAT_GPT_KEY_RELEASE=\"$CHAT_GPT_KEY_RELEASE\"" >> ./lingshot-keys.properties
echo "CHAT_GPT_KEY_DEBUG=\"$CHAT_GPT_KEY_DEBUG\"" >> ./lingshot-keys.properties
echo "GOOGLE_AUTH_ID=\"$GOOGLE_AUTH_ID\"" >> ./lingshot-keys.properties
echo "ADMOB_APP_ID_RELEASE=\"$ADMOB_APP_ID_RELEASE\"" >> ./lingshot-keys.properties
echo "ADMOB_APP_ID_DEBUG=\"$ADMOB_APP_ID_DEBUG\"" >> ./lingshot-keys.properties
echo "ADMOB_INTERSTITIAL_ID_RELEASE=\"$ADMOB_INTERSTITIAL_ID_RELEASE\"" >> ./lingshot-keys.properties
echo "ADMOB_INTERSTITIAL_ID_DEBUG=\"$ADMOB_INTERSTITIAL_ID_DEBUG\"" >> ./lingshot-keys.properties
echo "$FIREBASE_GOOGLE_SERVICES" >> ./app/src/google-services.json
echo "$RELEASE_KEY" | base64 -di >> ./keystore/lingshot-key
