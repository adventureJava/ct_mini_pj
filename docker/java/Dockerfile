FROM openjdk:11
WORKDIR /app

# Copy start.sh script
COPY start.sh /app
RUN chmod +x /app/start.sh
RUN chmod 777 /app

# Start with an entrypoint that waits for Java files
ENTRYPOINT ["/app/start.sh"]