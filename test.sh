awk -f lc.awk $(find helloworld-producer-api/src/main -name "*.scala") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee helloworld-producer-api/target/stats.log
awk -f lc.awk $(find helloworld-producer-impl/src/main -name "*.scala" -or -name "*.scala.html") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee helloworld-producer-impl/target/stats.log

awk -f lc.awk $(find helloworld-consumer-api/src/main -name "*.scala") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee helloworld-consumer-api/target/stats.log
awk -f lc.awk $(find helloworld-consumer-impl/src/main -name "*.scala" -or -name "*.scala.html") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee helloworld-consumer-impl/target/stats.log
awk -f lc.awk $(find twitter-producer-api/src/main -name "*.scala") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee twitter-producer-api/target/stats.log
awk -f lc.awk $(find twitter-producer-impl/src/main -name "*.scala" -or -name "*.scala.html") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee twitter-producer-impl/target/stats.log
awk -f lc.awk $(find twitter-consumer-api/src/main -name "*.scala") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee twitter-consumer-api/target/stats.log
awk -f lc.awk $(find twitter-consumer-impl/src/main -name "*.scala" -or -name "*.scala.html") 2>&1 | sed -r 's/'$(echo -e "\033")'\[[0-9]{1,2}(;([0-9]{1,2})?)?[mK]//g' | tee twitter-consumer-impl/target/stats.log
