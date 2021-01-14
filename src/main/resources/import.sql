# SurveyTemplates
insert into surveytemplate(id, name, description) values(10001, 'Veterans Administration - DSO', 'Primary survey for the VA');
insert into surveytemplate(id, name, description) values(10002, 'NIH - DSO', 'Specialized for health domain');

# Questions
insert into question(id, text) values(10001,'First question');
insert into question(id, text) values(10002,'Second question');
insert into question(id, text) values(10003,'Third question');
insert into question(id, text) values(10004,'Fourth question');
insert into question(id, text) values(10005,'Fifth question');

insert into surveytemplate_question(surveytemplate_ID, question_ID, question_order) values(10001, 10001, 1);
insert into surveytemplate_question(surveytemplate_ID, question_ID, question_order) values(10001, 10002, 2);
insert into surveytemplate_question(surveytemplate_ID, question_ID, question_order) values(10001, 10003, 3);
insert into surveytemplate_question(surveytemplate_ID, question_ID, question_order) values(10002, 10001, 1);
insert into surveytemplate_question(surveytemplate_ID, question_ID, question_order) values(10002, 10004, 2);


