Disini saya membuat api simple untuk apps Blog:
1. untuk run mungkin dalam local bisa dalam file BlogApplication
2. untuk documentation saya menggunakan swagger jadi setelah run project bisa menuju ke link http://localhost:8080/swagger-ui/index.html#/
3. untuk docker mungkin anda bisa men set untuk JWT_SECRET dan JWT_EXPIRATION dengan melakukan:
    a.  JWT_SECRET=rahasia JWT_EXPIRATION_IN_SECOND=86400 docker compose up(linux/ubuntu)
    b.  set JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
        set JWT_EXPIRATION=86400000
        docker-compose up
