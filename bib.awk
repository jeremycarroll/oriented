$0 ~ /^ *\[/ {  if (id) {
                     print "*/"
                     
                     print "    public static final String " camelCase(id) " = \"" title "\";"
                }

               print "/**"
               id = substr($1,1+index($1,"["))
               titleStart = index($2,").")+2
               title = substr($2,titleStart)
               titleEnd = index(title,".")
               if (titleEnd) {
                 title = substr(title,1,titleEnd-1)
               }
             }
{ print }


function camelCase(id) {
     nWords = split(id,words,"[ ,.][ ,.]*")
     result = tolower(substr(words[1],1,1)) substr(words[1],2)
     for ( i = 2; i<= nWords; i++ ) {
        result = result capitalize(words[i])
     }
     return result
}


function capitalize(w) {
    first = substr(w,1,1)
    if ( first ~ /[a-z]/ ) {
        return toupper(first) substr(w,2)
    }
    return w
}
