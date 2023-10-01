# foto-speurtocht
## dev users
### team
username: team-1

password: password
### admin
username: admin

password: admin
## API Docs
http://localhost:3080/api/swagger-ui/index.html#/

## cli flags
--zip="./speurtocht-88 dry run.zip" 
to load an already created file into the database with the following structure.
- challenges.tsv
- locations
  - with numbers as filenames for their position in the bingo card and only png's (because that was the only format that worked)
- teams.tsv

--username="admin"
&
--password="admin"

to set admin credentials in prod
