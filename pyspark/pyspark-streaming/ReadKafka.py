from pyspark.sql import SparkSession
import json

def getSparkSession():
    spark = SparkSession \
        .builder \
        .appName("MetricsAnalysis") \
        .getOrCreate()
    return spark

def subscribeKafkaTopic(spark, servers, topic):
    df = spark \
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", servers) \
        .option("subscribe", topic) \
        .load()
    df.selectExpr("CAST(key AS STRING)")
    return df

def extract_data(line, fields):
    data = json.load(line)
    extracted = []

    for field in fields:
        extracted.append(data[field])


def main():
    spark = getSparkSession()

    brokers = "mykafka:9092"
    topic = 'wpa_clicks'

    df = subscribeKafkaTopic(spark, brokers.join(','), topic)




if __name__ == '__main__':
    main()
