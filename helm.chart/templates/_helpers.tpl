{{/* _helpers.tpl */}}
{{- define "k8s.currentDate" -}}
{{- now | date "2006-01-02" | quote -}}
{{- end -}}
{{- define "k8s.version" -}}
{{- printf "%s" "1.0.0" | quote -}}
{{- end -}}
